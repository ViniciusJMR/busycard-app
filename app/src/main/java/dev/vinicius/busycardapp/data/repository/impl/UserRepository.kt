package dev.vinicius.busycardapp.data.repository.impl

import android.util.Log
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.vinicius.busycardapp.domain.model.user.User
import dev.vinicius.busycardapp.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(

): IUserRepository<String, User> {
    private val database = Firebase.firestore

    companion object {
        private const val TAG = "UserRepository"
    }

    override suspend fun saveMyCardId(userId: String, cardId: String) {
        database.collection("users")
            .document(userId)
            .update("myCards", FieldValue.arrayUnion(cardId))
    }

    override suspend fun getMyCardsId(userId: String): List<String> {
        var myCards: List<String> = emptyList()

        val task = database.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    myCards = documentSnapshot.get("myCards") as? List<String> ?: emptyList()
                }
            }

        task.await()

        return myCards
    }

    override suspend fun saveSharedCardId(userId: String, cardId: String) {
        database.collection("users")
            .document(userId)
            .update("sharedCards", FieldValue.arrayUnion(cardId))
            .await()
    }

    override suspend fun removeSharedCardId(userId: String, cardId: String) {
        database.collection("users")
            .document(userId)
            .update("sharedCards", FieldValue.arrayRemove(cardId))
            .await()
    }

    override suspend fun getSharedCardsId(userId: String): List<String> {
        var sharedCards: List<String> = emptyList()

        val task = database.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    sharedCards = documentSnapshot.get("sharedCards") as? List<String> ?: emptyList()
                }
            }

        task.await()

        return sharedCards
    }

    override suspend fun getUser(userId: String): User {
        var user: User? = null
        val task = database.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                user = documentSnapshot.toObject(User::class.java)
            }
        task.await()

        return user!!
    }

    override suspend fun save(item: User): String {
        database.collection("users").document(item.id).set(item).await()
        return item.id
    }

    override suspend fun getById(id: String): Flow<User> = flow {
        val task = database.collection("users").document(id).get()
        task.await()

        when {
            task.isSuccessful -> {
                val user = task.result.toObject(User::class.java)
                emit(user!!)
            }
            task.isCanceled -> TODO()
        }
    }

    override suspend fun saveDraftCardId(userId: String, cardId: String) {
        Log.d(TAG, "saveDraftCardId: $userId, $cardId")
        database.collection("users")
            .document(userId)
            .update("draftCards", FieldValue.arrayUnion(cardId))
            .await()
    }

    override suspend fun getDraftCardsId(userId: String): List<String> {
        var draftCards: List<String> = emptyList()

        val task = database.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    draftCards = documentSnapshot.get("draftCards") as? List<String> ?: emptyList()
                }
            }

        task.await()

        return draftCards
    }

    override suspend fun removeDraftCardId(userId: String, cardId: String) {
        database.collection("users")
            .document(userId)
            .update("sharedCards", FieldValue.arrayRemove(cardId))
            .await()
    }

    // Should Not be used
    override suspend fun getAll(): Flow<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(item: User) {
        TODO("Not yet implemented")
    }

    override suspend fun get(item: User): Flow<User> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getByIds(ids: List<String>): Flow<List<User>> {
        TODO("Not yet implemented")
    }
}