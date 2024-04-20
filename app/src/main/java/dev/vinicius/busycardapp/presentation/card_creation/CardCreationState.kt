package dev.vinicius.busycardapp.presentation.card_creation

import android.net.Uri
import dev.vinicius.busycardapp.domain.model.card.Field

data class CardCreationState (
    var cardFields: MutableList<Field> = mutableListOf(),
    var cardName: String = "",
    var cardImageUri: Uri? = null,
    var currentlySelectedField: Field? = null,
    var mainContactField: Field.TextField? = null,

    var isLoading: Boolean = false,
    var showBottomSheet: Boolean = false,
    var showTextTypeDialog: Boolean = false,
    var showCardInfoDialog: Boolean = false,
    var error: String? = null,
)