package dev.vinicius.busycardapp.presentation.auth.login

sealed class LoginEvent {
    data class OnEmailChange(val email: String): LoginEvent()
    data class OnPasswordChange(val password: String): LoginEvent()

    object OnLogin: LoginEvent()
    object OnSignInClicked: LoginEvent()
}