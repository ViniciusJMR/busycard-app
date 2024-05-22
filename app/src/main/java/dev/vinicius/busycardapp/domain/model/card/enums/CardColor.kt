package dev.vinicius.busycardapp.domain.model.card.enums

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

enum class CardColor (val color: Long) {
    Black(0xFF000000),
    DarkGray(0xFF444444),
    Gray(0xFF888888),
    LightGray(0xFFCCCCCC),
    White(0xFFFFFFFF),
    Red(0xFFFF0000),
    Green(0xFF00FF00),
    Blue(0xFF0000FF),
    Yellow(0xFFFFFF00),
    Cyan(0xFF00FFFF),
    Magenta(0xFFFF00FF),
}