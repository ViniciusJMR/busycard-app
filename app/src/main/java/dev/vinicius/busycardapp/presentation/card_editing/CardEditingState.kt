package dev.vinicius.busycardapp.presentation.card_editing

import android.net.Uri
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.enums.CardColor
import dev.vinicius.busycardapp.domain.model.card.enums.CardSize

data class CardEditingState (
    var cardId: String? = null,
    var cardFields: MutableList<Field> = mutableListOf(),
    var cardName: String = "",
    var cardImageUri: Uri? = null,
    var currentlySelectedField: Field? = null,
    var mainContactField: Field.TextField? = null,
    var isDraft: Boolean = false,
    var cardSize: CardSize = CardSize.SMALL,
    var cardColor: CardColor = CardColor.DarkGray,

    var isScreenLoading: Boolean = false,
    var isSaveLoading: Boolean = false,
    var showBottomSheet: Boolean = false,
    var showTextTypeDialog: Boolean = false,
    var showCardInfoDialog: Boolean = false,
    val showSaveDialog: Boolean = false,
    var error: String? = null,
)