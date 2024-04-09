package dev.vinicius.busycardapp.data.remote.firebase.model

import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.FieldType
import dev.vinicius.busycardapp.domain.model.card.TextType

// Currently unused as firebase converter can't deserialized this class
sealed class FirebaseFieldModel(
    open var type: FieldType? = null,
    open var name: String?  = null,
    open var offsetX: Float? = null,
    open var offsetY: Float? = null,
    open var size: Float? = null,
) {
    data class AddressField(
        override var name: String? = null,
        override var offsetX: Float? = null,
        override var offsetY: Float? = null,
        override var size: Float? = null,
        var localization: Pair<Long, Long>? = null,
        var textLocalization: String? = null,
    ): FirebaseFieldModel(FieldType.ADDRESS, name, offsetX, offsetY, size)

    data class ImageField(
        override var name: String? = null,
        override var offsetX: Float? = null,
        override var offsetY: Float? = null,
        override var size: Float? = null,
        var imageUrl: String? = null,
    ): FirebaseFieldModel(FieldType.IMAGE, name, offsetX, offsetY, size)

    data class TextField(
        override var name: String? = null,
        override var offsetX: Float? = null,
        override var offsetY: Float? = null,
        override var size: Float? = null,
        var textType: TextType? = null,
        var value: String? = null,
    ): FirebaseFieldModel(FieldType.TEXT, name, offsetX, offsetY, size)

}