package dev.vinicius.busycardapp.presentation.search_card

import dev.vinicius.busycardapp.domain.model.card.Card

data class SearchCardsState (
    val cards: List<Card> = emptyList(),

    val isLoading: Boolean = true,
)