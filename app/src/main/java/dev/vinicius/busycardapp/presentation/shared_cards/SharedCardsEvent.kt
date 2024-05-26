package dev.vinicius.busycardapp.presentation.shared_cards

sealed class SharedCardsEvent {
    data class OnSearchQueryChange(val query: String) : SharedCardsEvent()

    data object Refresh : SharedCardsEvent()
}
