package dev.vinicius.busycardapp.data.repository.impl

import android.util.Log
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.vinicius.busycardapp.data.remote.firebase.db.mapper.mapDomainFieldsToFirebaseModel
import dev.vinicius.busycardapp.data.remote.firebase.db.mapper.mapToDomainModel
import dev.vinicius.busycardapp.data.remote.firebase.db.mapper.mapToFirebaseModel
import dev.vinicius.busycardapp.data.remote.firebase.db.model.FirebaseCardModel
import dev.vinicius.busycardapp.domain.repository.IRepository
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.repository.Bucket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val bucket: Bucket
): IRepository<String, Card> {

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

        item.image.uri = item.image.uri?.let {
            val ext = it.path!!.substring(it.path!!.lastIndexOf(".") + 1)
            val bucketPath = "cards/$key/cardImage.${ext}"
            bucket.uploadFile(it, bucketPath)
        }
        Log.d(TAG, "save: uri: ${item.image.uri} - ${item.image.uri?.path}")

        item.fields
            .filterIsInstance<Field.ImageField>()
            .forEach {
                val uri = it.image.uri!!
                val ext = uri.path!!.substring(uri.path!!.lastIndexOf(".") + 1)
                val imageId = uri.toString().substring(uri.toString().lastIndexOf("%") + 1)

                val bucketPath = "fields/$key/$imageId.$ext"
                it.image.uri = bucket.uploadFile(uri, bucketPath)
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