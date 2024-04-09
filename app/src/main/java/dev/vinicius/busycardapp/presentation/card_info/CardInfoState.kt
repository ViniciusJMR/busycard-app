package dev.vinicius.busycardapp.presentation.card_info

import dev.vinicius.busycardapp.domain.model.card.Card

data class CardInfoState(
    val card: Card? = null,
    val text: String = "",

    val isLoading: Boolean = false,
)
