package dev.vinicius.busycardapp.presentation.card_detail

sealed class CardInfoEvent {

    sealed class CardEvent: CardInfoEvent() {
        data object OnSaveToSharedCard: CardEvent()
        data object OnDeleteFromSharedCard: CardEvent()
    }

    sealed class DialogEvent: CardInfoEvent() {
        data object OnShowShareDialog: DialogEvent()

        data object OnDismissShareDialog: DialogEvent()
    }

    sealed class ModalEvent: CardInfoEvent() {
        data object OnShowBottomSheet: ModalEvent()
        data object OnDismissModalSheet: ModalEvent()
    }
}