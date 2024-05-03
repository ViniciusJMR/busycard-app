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

    var currentUser: FirebaseUser? = auth.currentUser

    override fun getCurrentUserId(): String = currentUser?.uid ?: ""

    override fun isLogged() = currentUser != null

    override suspend fun logIn(email: String, password: String) {
        val authTask = auth.signInWithEmailAndPassword(email, password)
        authTask.await()

        when {
            authTask.isSuccessful -> {
                currentUser = authTask.result.user
            }
            authTask.isCanceled -> {
                Log.e(TAG, "login: Error when trying to login. ${authTask.exception?.message}")
                authTask.exception?.printStackTrace()
            }

        }

    }

    override fun logOut() {
        auth.signOut()
        currentUser = null
    }

    override suspend fun signIn(email: String, password: String) {
        val authTask = auth.createUserWithEmailAndPassword(email, password)
        authTask.await()

        when {
            authTask.isSuccessful -> {
                currentUser = authTask.result.user
            }
            authTask.isCanceled -> {
                Log.e(TAG, "login: Error when trying to create account. ${authTask.exception?.message}")
                authTask.exception?.printStackTrace()
            }
        }
    }
}