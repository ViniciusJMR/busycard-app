package dev.vinicius.busycardapp.domain.model.card

sealed class Field(
    open var name: String = "",
    open var offsetX: Float = 0f,
    open var offsetY: Float = 0f,
    open var size: Float = 0f,
) {
    data class AddressField(
        override var name: String = "",
        override var offsetX: Float = 0f,
        override var offsetY: Float = 0f,
        override var size: Float = 0f,
        var localization: Pair<Long, Long>,
        var textLocalization: String = "",
    ): Field(name, offsetX, offsetY, size)

    data class ImageField(
        override var name: String = "",
        override var offsetX: Float = 0f,
        override var offsetY: Float = 0f,
        override var size: Float = 0f,
        var image: CardImage = CardImage()
    ): Field(name, offsetX, offsetY, size)

    data class TextField(
        override var name: String = "",
        override var offsetX: Float = 0f,
        override var offsetY: Float = 0f,
        override var size: Float = 0f,
        var textType: TextType = TextType.TEXT,
        var value: String = "",
    ): Field(name, offsetX, offsetY, size)
}
