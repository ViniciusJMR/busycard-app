package dev.vinicius.busycardapp.presentation.card_creation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import dev.vinicius.busycardapp.domain.model.card.Field

data class CardCreationState (
    var cardFields: MutableList<Field> = mutableListOf(),
    var cardName: String = "",
    var currentlySelectedField: Field? = null,

    var isLoading: Boolean = false,
    var showBottomSheet: Boolean = false,
    var showTextTypeDialog: Boolean = false,
    var showCardInfoDialog: Boolean = false,
    var error: String? = null,
)