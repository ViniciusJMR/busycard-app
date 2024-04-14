package dev.vinicius.busycardapp.data.repository.impl

import android.util.Log
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.vinicius.busycardapp.data.remote.firebase.mapper.mapToDomainModel
import dev.vinicius.busycardapp.data.remote.firebase.mapper.mapToFirebaseModel
import dev.vinicius.busycardapp.data.remote.firebase.model.FirebaseCardModel
import dev.vinicius.busycardapp.data.repository.Repository
import dev.vinicius.busycardapp.domain.model.card.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

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
                val cards = firebaseCards?.map { it.mapToDomainModel() } ?: emptyList()
                emit(cards)
            }
            dataSnapshot.isCanceled -> {
                TODO()
            }
        }
    }

    override suspend fun getById(id: String): Flow<Card> = flow {
        val dataSnapshot = database.child("cards").child(id).get()
        dataSnapshot.await()

        when {
            dataSnapshot.isSuccessful -> {
                val firebaseCard = dataSnapshot.result.getValue<FirebaseCardModel>()
                val card = firebaseCard!!.mapToDomainModel()
                emit(card)
            }
            dataSnapshot.isCanceled -> {
                TODO()
            }
        }
    }

    override suspend fun save(item: Card) {
        val key = database.child("cards").push().key
        item.id = key

        // Firebase saves 0 as Long and this will cause an exception when mapping back
        item.fields.forEach{
            it.size = if (it.size == 0f ) 0.0001f else it.size
        }

        database
            .child("cards")
            .child(item.id.toString())
            .setValue(item.mapToFirebaseModel())
    }

}