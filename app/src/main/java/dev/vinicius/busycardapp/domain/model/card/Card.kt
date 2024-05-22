package dev.vinicius.busycardapp.domain.model.card

import dev.vinicius.busycardapp.domain.model.card.enums.CardSize
import dev.vinicius.busycardapp.domain.model.card.enums.CardState

data class Card(
    var id: String? = null,
    var name: String = "",
    var owner: String? = null,
    var mainContact: String = "",
    var image: CardImage = CardImage(),
    val fields: List<Field> = emptyList(),
    var cardState: CardState = CardState.NOT_SHARED,
    var isDraft: Boolean = false,
    var cardSize: CardSize = CardSize.SMALL,
)
