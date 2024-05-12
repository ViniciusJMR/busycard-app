package dev.vinicius.busycardapp.presentation.card_detail

import dev.vinicius.busycardapp.domain.model.card.Field

data class CardDetailState(
    val id: Long? = null,
    var name: String = "",
    var owner: String? = "",
    val fields: List<Field> = emptyList(),
    val text: String = "",

    val isLoading: Boolean = false,
    val showBottomSheet: Boolean = false,
    val showShareDialog: Boolean = false,
)
