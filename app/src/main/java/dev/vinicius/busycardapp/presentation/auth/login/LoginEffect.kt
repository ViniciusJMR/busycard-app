package dev.vinicius.busycardapp.presentation.auth.login

sealed class LoginEffect {

    object NavigateToHome : LoginEffect()

    object NavigateToCreateAccount: LoginEffect()
}