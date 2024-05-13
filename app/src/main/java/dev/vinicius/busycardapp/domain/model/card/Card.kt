package dev.vinicius.busycardapp.domain.model.card

import android.net.Uri

data class Card(
    var id: String? = null,
    var name: String = "",
    var owner: String? = null,
    var mainContact: String = "",
    var image: CardImage = CardImage(),
    val fields: List<Field>,
    var cardState: CardState = CardState.NOT_SHARED,
)
