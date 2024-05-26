package dev.vinicius.busycardapp.presentation.my_cards

sealed class MyCardsEvent{
    data class OnSearchQueryChange(val query: String) : MyCardsEvent()

    data object Refresh : MyCardsEvent()
}
