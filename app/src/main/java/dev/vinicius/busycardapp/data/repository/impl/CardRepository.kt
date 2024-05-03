package dev.vinicius.busycardapp.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
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
import java.util.UUID
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val bucket: Bucket
): IRepository<String, Card> {

    // TODO: move it to interface
    private val database = Firebase.firestore

    companion object {
        val TAG = "CardRepository"
    }

    override suspend fun getByIds(ids: List<String>): Flow<List<Card>> = flow{
        emit(database.collection("cards")
            .whereIn("id", ids)
            .get()
            .await()
            .map { it.toObject(FirebaseCardModel::class.java).mapToDomainModel(emptyList()) }
        )
    }

    override suspend fun getAll(): Flow<List<Card>> = flow {
        val dataSnapshot = database.collection("cards").get()
        dataSnapshot.await()

        when {
            dataSnapshot.isSuccessful -> {
                val firebaseCardsMap = dataSnapshot.result.toObjects(FirebaseCardModel::class.java)
                // TODO: Create new Card model for Card Meta data
                val cards = firebaseCardsMap.map { it.mapToDomainModel(emptyList()) }
                emit(cards)
            }
            dataSnapshot.isCanceled -> {
                TODO()
            }
        }
    }

    override suspend fun getById(id: String): Flow<Card> = flow {
        val cardsSnapshot = database.collection("cards").document(id).get()
        val fieldsSnapshot = database.collection("fields").document(id).get()
        cardsSnapshot.await()
        fieldsSnapshot.await()

        when {
            cardsSnapshot.isSuccessful -> {
                val firebaseCard = cardsSnapshot.result.toObject(FirebaseCardModel::class.java)
                Log.d(TAG, "getById: fields: ${fieldsSnapshot.result.data?.get("list")}")
                val firebaseFields: List<Map<String, Any>> = fieldsSnapshot.result.data?.get("list") as List<Map<String, Any>>
                val card = firebaseCard!!.mapToDomainModel(firebaseFields)
                emit(card)
            }
            cardsSnapshot.isCanceled -> {
                TODO()
            }
        }
    }

    override suspend fun save(item: Card): String {
        database.collection("cards")
        val key = UUID.randomUUID().toString()
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
            .collection("cards")
            .document(item.id.toString())
            .set(item.mapToFirebaseModel())


        database
            .collection("fields")
            .document(item.id.toString())
            .set(mapOf("list" to mapDomainFieldsToFirebaseModel(item.fields)))

        return key
    }

}