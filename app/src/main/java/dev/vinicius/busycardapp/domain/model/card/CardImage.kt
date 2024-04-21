package dev.vinicius.busycardapp.domain.model.card

import android.net.Uri

data class CardImage(
    var uri: Uri? = null,
    val path: String = uri?.path.orEmpty(),
)
