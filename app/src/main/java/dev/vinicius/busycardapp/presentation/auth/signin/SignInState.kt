package dev.vinicius.busycardapp.presentation.auth.signin

data class SignInState(
    val email: String = "",
    val password: String = "",
    val password2: String = "",
    var username: String = "",
    var name: String = "",
    var surname: String = "",
)