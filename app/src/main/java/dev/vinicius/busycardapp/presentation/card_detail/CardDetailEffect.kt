package dev.vinicius.busycardapp.presentation.card_detail

sealed class CardDetailEffect{
    object ClosePage: CardDetailEffect()

    data class ShowSnackbar(val message: String): CardDetailEffect()
}