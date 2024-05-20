package dev.vinicius.busycardapp.domain.model.card

import com.google.android.gms.maps.model.LatLng
import dev.vinicius.busycardapp.domain.model.card.enums.TextType


sealed class Field(
    open var name: String = "",
    open var offsetX: Int= 0,
    open var offsetY: Int= 0,
    open var size: Int= 0,
) {
    data class AddressField(
        override var name: String = "",
        override var offsetX: Int= 0,
        override var offsetY: Int= 0,
        override var size: Int= 0,
        var localization: LatLng? = null,
        var textLocalization: String = "",
    ): Field(name, offsetX, offsetY, size)

    data class ImageField(
        override var name: String = "",
        override var offsetX: Int= 0,
        override var offsetY: Int= 0,
        override var size: Int= 0,
        var image: CardImage = CardImage()
    ): Field(name, offsetX, offsetY, size)

    data class TextField(
        override var name: String = "",
        override var offsetX: Int= 0,
        override var offsetY: Int= 0,
        override var size: Int= 0,
        var textType: TextType = TextType.TEXT,
        var value: String = "",
    ): Field(name, offsetX, offsetY, size)
}
