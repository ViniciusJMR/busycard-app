package dev.vinicius.busycardapp.presentation.search_card

sealed class SearchCardsEvent {
    data class OnSearchQueryChange(val query: String) : SearchCardsEvent()

    data object Refresh : SearchCardsEvent()
}
