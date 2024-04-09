package dev.vinicius.busycardapp.presentation.card_info

import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.card.Field

data class CardInfoState(
    val id: Long? = null,
    var name: String = "",
    var owner: String = "",
    val fields: List<Field> = emptyList(),
    val text: String = "",

    val isLoading: Boolean = false,
)
