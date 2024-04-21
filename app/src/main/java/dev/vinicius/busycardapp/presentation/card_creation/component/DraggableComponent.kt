package dev.vinicius.busycardapp.presentation.card_creation.component

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import dev.vinicius.busycardapp.domain.model.card.Field
import kotlin.math.roundToInt

@Composable
fun DraggableFieldComponent(
    modifier: Modifier = Modifier,
    initialX: Int,
    initialY: Int,
    onDragField: (Int, Int) -> Unit,
    onTapField: () -> Unit,
    content: @Composable () -> Unit
) {
    //TODO: Mudar para que o param onDragField seja o responsável por atualizar o field, não sendo necessário passar o field

    var offsetX by remember { mutableIntStateOf(initialX) }
    var offsetY by remember { mutableIntStateOf(initialY) }
    Box(
        content  = { content() },
        modifier = modifier
            .offset {
                IntOffset(
                    offsetX,
                    offsetY,
                )
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x.roundToInt()
                    offsetY += dragAmount.y.roundToInt()
                    onDragField(offsetX, offsetY)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures {
                    onTapField()
                }
            },
    )
}
