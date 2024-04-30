package dev.vinicius.busycardapp.data.repository.impl

import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.vinicius.busycardapp.domain.model.user.User
import dev.vinicius.busycardapp.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(

): IUserRepository<String, User> {
    private val database = Firebase.database.reference

    override fun saveMyCardId(userId: String, cardId: String) {
//        database.child("users")
//            .child(userId)
//            .child("myCards")
//            .child(cardId)
//            .setValue(true) // Suggested form to save a list by firebase
    }

    override suspend fun getAll(): Flow<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun save(item: User) {
        database.child("users").child(item.id).setValue(item)
    }

    override suspend fun getById(id: String): Flow<User> = flow {
        val task = database.child("users").child(id).get()
        task.await()

        when {
            task.isSuccessful -> {
                val user = task.result.getValue<User>()
                emit(user!!)
            }
            task.isCanceled -> TODO()
        }
    }
}