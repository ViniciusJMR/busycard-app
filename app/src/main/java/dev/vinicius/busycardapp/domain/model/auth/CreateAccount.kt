package dev.vinicius.busycardapp.domain.model.auth

data class CreateAccount(
    var username: String = "",
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var password: String = "",
    var password2: String = "",
)
