package dev.vinicius.busycardapp.presentation.auth.signin

import dev.vinicius.busycardapp.presentation.auth.login.LoginEffect

sealed class SignInEffect {
    object NavigateToHome : SignInEffect()
}