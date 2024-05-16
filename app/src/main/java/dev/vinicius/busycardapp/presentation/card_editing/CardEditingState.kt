package dev.vinicius.busycardapp.presentation.card_editing

import android.net.Uri
import dev.vinicius.busycardapp.domain.model.card.Field

data class CardEditingState (
    var cardId: String? = null,
    var cardFields: MutableList<Field> = mutableListOf(),
    var cardName: String = "",
    var cardImageUri: Uri? = null,
    var currentlySelectedField: Field? = null,
    var mainContactField: Field.TextField? = null,

    var isScreenLoading: Boolean = false,
    var showBottomSheet: Boolean = false,
    var showTextTypeDialog: Boolean = false,
    var showCardInfoDialog: Boolean = false,
    var error: String? = null,
)