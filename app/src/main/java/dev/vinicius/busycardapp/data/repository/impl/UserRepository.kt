package dev.vinicius.busycardapp.data.repository.impl

import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
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
                    myCards = documentSnapshot.get("myCards") as List<String>
                }
            }

        task.await()

        return myCards
    }

    override suspend fun save(item: User) {
        database.collection("users").document(item.id).set(item).await()
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
}