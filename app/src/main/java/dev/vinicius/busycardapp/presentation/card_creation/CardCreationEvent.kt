package dev.vinicius.busycardapp.presentation.card_creation

import android.net.Uri
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.FieldType
import dev.vinicius.busycardapp.domain.model.card.TextType

sealed class CardCreationEvent {
    sealed class CardEvent: CardCreationEvent() {
        data class OnAddField(val fieldType: FieldType): CardEvent()
        data class OnDeleteField(val field: Field): CardEvent()
        data class OnSelectField(val field: Field?): CardEvent()


        object OnSaveCard: CardEvent()

        sealed class OnChangeCard: CardEvent() {
            data class Info(
                val name: String = "",
                val imagePath: Uri? = null,
            ): OnChangeCard()

            data class MainContact(val field: Field.TextField): OnChangeCard()
        }

    }

    sealed class FieldEvent: CardCreationEvent() {
        data class OnTextFieldChange(
            var name: String? = null,
            var offsetX: Float? = null,
            var offsetY: Float? = null,
            var size: Float? = null,
            var textType: TextType? = null,
            var value: String? = null,
        ): FieldEvent()
        data class OnTextFieldTypeChange(val textType: TextType): FieldEvent()
    }

    sealed class ModalEvent: CardCreationEvent() {
        object OnDismissModalSheet: ModalEvent()
        data class OnShowBottomSheet(val field: Field? = null): ModalEvent()
    }

    sealed class DialogEvent: CardCreationEvent() {
        object OnShowTextTypeDialog: DialogEvent()
        object OnDismissTextTypeDialog: DialogEvent()

        object OnShowCardInfoDialog: DialogEvent()
        object OnDismissCardInfoDialog: DialogEvent()

    }

}

