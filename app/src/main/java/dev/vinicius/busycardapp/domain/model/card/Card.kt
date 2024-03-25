package dev.vinicius.busycardapp.domain.model.card

data class Card(
    val id: Long,
    var name: String,
    var owner: String,
    val fields: List<Field>,
)
