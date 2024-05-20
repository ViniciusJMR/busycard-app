package dev.vinicius.busycardapp.presentation.card_detail

import dev.vinicius.busycardapp.domain.model.card.enums.CardState
import dev.vinicius.busycardapp.domain.model.card.Field

data class CardDetailState(
    val id: String = "",
    var name: String = "",
    var owner: String? = "",
    var imagePath: String = "",
    val cardState: CardState = CardState.NOT_SHARED,
    val fields: List<Field> = emptyList(),
    val text: String = "",

    val isScreenLoading: Boolean = false,
    val showBottomSheet: Boolean = false,
    val isBottomSheetLoading: Boolean = false,
    val showShareDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
)
