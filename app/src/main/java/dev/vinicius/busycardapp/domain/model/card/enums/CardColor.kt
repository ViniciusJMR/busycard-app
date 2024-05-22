package dev.vinicius.busycardapp.domain.model.card.enums

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

enum class CardColor (val color: Long) {
    Black   (0XFF121111),
    DarkGray(0XFF444444),
    Gray    (0XFF888888),
    LightGray(0XFFCCCCCC),
    Red     (0XFFFF0000),
    Green   (0XFF04D404),
    Blue    (0XFF0909E0),
    Yellow  (0XFFE3E30B),
    Cyan    (0XFF08C9C9),
}