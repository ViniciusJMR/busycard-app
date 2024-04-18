package dev.vinicius.busycardapp.data.repository.impl

import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.vinicius.busycardapp.data.remote.firebase.db.mapper.mapDomainFieldsToFirebaseModel
import dev.vinicius.busycardapp.data.remote.firebase.db.mapper.mapToDomainModel
import dev.vinicius.busycardapp.data.remote.firebase.db.mapper.mapToFirebaseModel
import dev.vinicius.busycardapp.data.remote.firebase.db.model.FirebaseCardModel
import dev.vinicius.busycardapp.data.repository.Repository
import dev.vinicius.busycardapp.domain.model.card.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CardRepository @Inject constructor(
): Repository<String, Card> {

    // TODO: move it to interface
    private val database = Firebase.database.reference

    companion object {
        val TAG = "CardRepository"
    }

    override suspend fun getAll(): Flow<List<Card>> = flow {
        val dataSnapshot = database.child("cards").get()
        dataSnapshot.await()

        when {
            dataSnapshot.isSuccessful -> {
                val firebaseCardsMap = dataSnapshot.result.getValue<HashMap<String, FirebaseCardModel>>()
                val firebaseCards = firebaseCardsMap?.entries?.map { it.value }
                // TODO: Create new Card model for Card Meta data
                val cards = firebaseCards?.map { it.mapToDomainModel(emptyList()) } ?: emptyList()
                emit(cards)
            }
            dataSnapshot.isCanceled -> {
                TODO()
            }
        }
    }

    override suspend fun getById(id: String): Flow<Card> = flow {
        val cardsSnapshot = database.child("cards").child(id).get()
        val fieldsSnapshot = database.child("fields").child(id).get()
        cardsSnapshot.await()
        fieldsSnapshot.await()

        when {
            cardsSnapshot.isSuccessful -> {
                val firebaseCard = cardsSnapshot.result.getValue<FirebaseCardModel>()
                val firebaseFields = fieldsSnapshot.result
                    .getValue<List<Map<String, Any>>>()
                    ?: emptyList()
                val card = firebaseCard!!.mapToDomainModel(firebaseFields)
                emit(card)
            }
            cardsSnapshot.isCanceled -> {
                TODO()
            }
        }
    }

    override suspend fun save(item: Card) {
        val key = database.child("cards").push().key
        item.id = key

        // Firebase saves 0 as Long and this will cause an exception when mapping back
        item.fields.forEach{
            it.size = if (it.size == 0f ) 0.01f else it.size
        }

        database
            .child("cards")
            .child(item.id.toString())
            .setValue(item.mapToFirebaseModel())

        database
            .child("fields")
            .child(item.id.toString())
            .setValue(mapDomainFieldsToFirebaseModel(item.fields))
    }

}