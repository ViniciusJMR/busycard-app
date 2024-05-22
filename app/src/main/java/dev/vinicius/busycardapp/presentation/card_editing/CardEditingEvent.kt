package dev.vinicius.busycardapp.presentation.card_editing

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.enums.CardSize
import dev.vinicius.busycardapp.domain.model.card.enums.FieldFont
import dev.vinicius.busycardapp.domain.model.card.enums.FieldType
import dev.vinicius.busycardapp.domain.model.card.enums.LocationIconPosition
import dev.vinicius.busycardapp.domain.model.card.enums.TextType

sealed class CardEditingEvent {
    sealed class CardEvent: CardEditingEvent() {
        data class OnAddField(val fieldType: FieldType): CardEvent()
        data class OnDeleteField(val field: Field): CardEvent()
        data class OnSelectField(val field: Field?): CardEvent()

        data class OnDragField(val field: Field, val offsetX: Int, val offsetY: Int): CardEvent()


        sealed class OnSaveEvent: CardEvent() {
            data object OnSaveCard: OnSaveEvent()
            data object OnSaveCardAsDraft: OnSaveEvent()
        }


        sealed class OnChangeCard: CardEvent() {
            data class Info(
                val name: String = "",
                val imagePath: Uri? = null,
                val size: CardSize = CardSize.SMALL,
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
            var font: FieldFont? = null,
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
            var font: FieldFont? = null,
            var iconSize : Int? = null,
            var iconPosition : LocationIconPosition? = null,
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

