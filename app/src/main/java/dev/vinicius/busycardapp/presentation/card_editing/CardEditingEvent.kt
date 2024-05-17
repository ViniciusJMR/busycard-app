package dev.vinicius.busycardapp.presentation.card_editing

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.FieldType
import dev.vinicius.busycardapp.domain.model.card.TextType
import dev.vinicius.busycardapp.presentation.card_detail.CardInfoEvent

sealed class CardEditingEvent {
    sealed class CardEvent: CardEditingEvent() {
        data class OnAddField(val fieldType: FieldType): CardEvent()
        data class OnDeleteField(val field: Field): CardEvent()
        data class OnSelectField(val field: Field?): CardEvent()


        sealed class OnSaveEvent: CardEvent() {
            data object OnSaveCard: OnSaveEvent()
            data object OnSaveCardAsDraft: OnSaveEvent()
        }


        sealed class OnChangeCard: CardEvent() {
            data class Info(
                val name: String = "",
                val imagePath: Uri? = null,
            ): OnChangeCard()

            data class MainContact(val field: Field.TextField): OnChangeCard()
        }

    }

    sealed class FieldEvent: CardEditingEvent() {
        data class OnTextFieldChange(
            var name: String? = null,
            var offsetX: Int? = null,
            var offsetY: Int? = null,
            var size: Int? = null,
            var textType: TextType? = null,
            var value: String? = null,
        ): FieldEvent()
        data class OnTextFieldTypeChange(val textType: TextType): FieldEvent()

        data class OnImageFieldChange(
            var name: String? = null,
            var offsetX: Int? = null,
            var offsetY: Int? = null,
            var size: Int? = null,
            var uri: Uri? = null,
        ): FieldEvent()

        data class OnAddressFieldChange(
            var name: String? = null,
            var offsetX: Int? = null,
            var offsetY: Int? = null,
            var size: Int? = null,
            var textLocalization: String? = null,
            var localization: LatLng? = null,
        ): FieldEvent()
    }

    sealed class ModalEvent: CardEditingEvent() {
        object OnDismissModalSheet: ModalEvent()
        data class OnShowBottomSheet(val field: Field? = null): ModalEvent()
    }

    sealed class DialogEvent: CardEditingEvent() {
        object OnShowTextTypeDialog: DialogEvent()
        object OnDismissTextTypeDialog: DialogEvent()

        object OnShowCardInfoDialog: DialogEvent()
        object OnDismissCardInfoDialog: DialogEvent()

        data object OnShowSaveDialog: DialogEvent()
        data object OnDismissSaveDialog: DialogEvent()

    }

}

