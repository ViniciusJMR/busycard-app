package dev.vinicius.busycardapp.presentation.auth.signin

import dev.vinicius.busycardapp.presentation.auth.login.LoginEvent

sealed class SignInEvent {
    data class OnEmailChange(val email: String): SignInEvent()
    data class OnPasswordChange(val password: String): SignInEvent()
    data class OnPassword2Change(val password: String): SignInEvent()
    data class OnNameChange(val name: String): SignInEvent()
    data class OnSurnameChange(val surname: String): SignInEvent()
    data class OnUsernameChange(val username: String): SignInEvent()
    object OnSignIn: SignInEvent()
}