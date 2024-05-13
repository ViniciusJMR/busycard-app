package dev.vinicius.busycardapp.domain.model.user

data class User (
    var id: String = "",
    var username: String = "",
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    val myCards: List<String> = emptyList(),
    val sharedCards: List<String> = emptyList(),
)