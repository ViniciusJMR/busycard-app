package dev.vinicius.busycardapp.presentation.shared_cards

import dev.vinicius.busycardapp.domain.model.card.Card

data class SharedCardsState (
    val cards: List<Card> = emptyList(),

    val isLoading: Boolean = true,
)