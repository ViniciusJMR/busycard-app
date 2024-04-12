package dev.vinicius.busycardapp.presentation.my_cards

import dev.vinicius.busycardapp.domain.model.card.Card

data class MyCardsState (
    val cards: List<Card> = emptyList(),

    val isLoading: Boolean = true,
)