package dev.vinicius.busycardapp.presentation.card_creation

import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.FieldType

sealed class CardCreationEvent {
    data class OnAddField(val fieldType: FieldType): CardCreationEvent()
    data class OnDeleteField(val field: Field): CardCreationEvent()
    data class OnSelectField(val field: Field?): CardCreationEvent()

    object OnDismissModalSheet: CardCreationEvent()
    data class OnShowBottomSheet(val field: Field? = null): CardCreationEvent()
}