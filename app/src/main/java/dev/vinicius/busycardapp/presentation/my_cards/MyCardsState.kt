package dev.vinicius.busycardapp.presentation.my_cards

import dev.vinicius.busycardapp.domain.model.card.Card

data class MyCardsState (
    val myCards: List<Card> = emptyList(),
    val draftCards: List<Card> = emptyList(),
    val searchQuery: String = "",

    val isMyCardsLoading: Boolean = true,
    val isDraftCardsLoading: Boolean = true,
)