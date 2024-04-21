package dev.vinicius.busycardapp.data.remote.firebase.db.model

import dev.vinicius.busycardapp.domain.model.card.FieldType
import dev.vinicius.busycardapp.domain.model.card.TextType

// Currently unused as firebase converter can't deserialized this class
sealed class FirebaseFieldModel(
    open var type: FieldType? = null,
    open var name: String?  = null,
    open var offsetX: Int? = null,
    open var offsetY: Int? = null,
    open var size: Int? = null,
) {
    data class AddressField(
        override var name: String? = null,
        override var offsetX: Int? = null,
        override var offsetY: Int? = null,
        override var size: Int? = null,
        var localization: Pair<Long, Long>? = null,
        var textLocalization: String? = null,
    ): FirebaseFieldModel(FieldType.ADDRESS, name, offsetX, offsetY, size)

    data class ImageField(
        override var name: String? = null,
        override var offsetX: Int? = null,
        override var offsetY: Int? = null,
        override var size: Int? = null,
        var imageUrl: String? = null,
    ): FirebaseFieldModel(FieldType.IMAGE, name, offsetX, offsetY, size)

    data class TextField(
        override var name: String? = null,
        override var offsetX: Int? = null,
        override var offsetY: Int? = null,
        override var size: Int? = null,
        var textType: TextType? = null,
        var value: String? = null,
    ): FirebaseFieldModel(FieldType.TEXT, name, offsetX, offsetY, size)

}