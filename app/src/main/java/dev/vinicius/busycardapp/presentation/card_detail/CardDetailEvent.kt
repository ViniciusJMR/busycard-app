package dev.vinicius.busycardapp.presentation.card_detail

sealed class CardInfoEvent {

    sealed class DialogEvent: CardInfoEvent() {
        data object OnShowShareDialog: DialogEvent()

        data object OnDismissShareDialog: DialogEvent()

    }
}