package dev.vinicius.busycardapp.presentation.card_detail

import dev.vinicius.busycardapp.domain.model.card.Field

data class CardDetailState(
    val id: String = "",
    var name: String = "",
    var owner: String? = "",
    val fields: List<Field> = emptyList(),
    val text: String = "",

    val isMyCard: Boolean = false,
    val isSharedCard: Boolean = false,

    val isScreenLoading: Boolean = false,
    val showBottomSheet: Boolean = false,
    val isBottomSheetLoading: Boolean = false,
    val showShareDialog: Boolean = false,
)
