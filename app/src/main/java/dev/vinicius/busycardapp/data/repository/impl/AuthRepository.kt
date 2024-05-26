package dev.vinicius.busycardapp.data.repository.impl

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.vinicius.busycardapp.domain.repository.Auth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
): Auth {
    companion object {
        val TAG = "AuthRepository"
    }

    private val auth = Firebase.auth

    override fun getCurrentUserId(): String = auth.currentUser?.uid ?: ""

    override fun isLogged() = auth.currentUser != null

    override suspend fun logIn(email: String, password: String) {
        val authTask = auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "successful login - ${auth.currentUser.toString()}")
                } else {
                    it.exception?.printStackTrace()
                    throw Exception(it.exception)
                }
            }
        authTask.await()

    }

    override fun logOut() {
        auth.signOut()
    }

    override suspend fun signIn(email: String, password: String) {
        val authTask = auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "successful login - ${auth.currentUser.toString()}")
                } else {
                    it.exception?.printStackTrace()
                    throw Exception(it.exception)
                }
            }
        authTask.await()
    }
}