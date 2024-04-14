package dev.vinicius.busycardapp.domain.model.card

data class Card(
    var id: String? = null,
    var name: String,
    var owner: String,
    val fields: List<Field>,
)
