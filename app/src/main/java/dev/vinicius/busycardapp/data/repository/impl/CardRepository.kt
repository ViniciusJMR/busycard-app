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
import kotlinx.coroutines.awaitAll
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
        val prodCardsTask = database.collection("cards")
            .whereIn("id", ids)
            .get()


        val draftCardsTask = database.collection("draftCards")
            .whereIn("id", ids)
            .get()

        val draftCards = draftCardsTask
            .await()
            .map { it.toObject(FirebaseCardModel::class.java).mapToDomainModel(emptyList()) }

        val prodCards = prodCardsTask
            .await()
            .map { it.toObject(FirebaseCardModel::class.java).mapToDomainModel(emptyList()) }

        val cards = prodCards + draftCards
        emit(cards)
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


    override suspend fun deleteById(id: String) {
        database.collection("cards").document(id).delete().await()
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
        val docCard: String
        val docFields: String

        if (!item.isDraft) {
            docCard = "cards"
            docFields = "fields"
        } else {
            docCard = "draftCards"
            docFields = "draftFields"
        }

        val key: String
        if (item.id != null) {
            key = item.id!!
        } else {
            key = UUID.randomUUID().toString()
            item.id = key
        }

        item.image.uri = item.image.uri?.let {
            val bucketPath = "cards/$key/cardImage"
            val subs = it.toString().substring(0, 4)
            Log.d(TAG, "save: substring ${subs}")
            if (subs.contains("http")) {
                return@let it
            }
            bucket.uploadFile(it, bucketPath)
        }
        Log.d(TAG, "save: uri: ${item.image.uri} - ${item.image.uri?.path}")

        item.fields
            .filterIsInstance<Field.ImageField>()
            .forEach {
                val uri = it.image.uri!!
                val imageId = uri.toString().substring(uri.toString().lastIndexOf("/") + 1)

                if (uri.toString().substring(0, 4).contains("http")) {
                    return@forEach
                }

                val bucketPath = "fields/$key/$imageId"
                it.image.uri = bucket.uploadFile(uri, bucketPath)
            }


        database
            .collection(docCard)
            .document(item.id.toString())
            .set(item.mapToFirebaseModel())


        database
            .collection(docFields)
            .document(item.id.toString())
            .set(mapOf("list" to mapDomainFieldsToFirebaseModel(item.fields)))

        return key
    }

}