package dev.vinicius.busycardapp.domain.repository

interface Auth {
    fun isLogged(): Boolean
    suspend fun logIn(email: String, password: String)
    fun logOut()
    suspend fun signIn(email: String, password: String)

    fun getCurrentUserId(): String
}