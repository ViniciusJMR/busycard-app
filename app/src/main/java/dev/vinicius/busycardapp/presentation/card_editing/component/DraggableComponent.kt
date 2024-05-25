package dev.vinicius.busycardapp.presentation.card_editing.component

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun DraggableFieldComponent(
    modifier: Modifier = Modifier,
    x: Int,
    y: Int,
    onDragField: (Int, Int) -> Unit,
    onTapField: () -> Unit,
    content: @Composable () -> Unit
) {
    val TAG = "DraggableFieldComponent"
    //TODO: Mudar para que o param onDragField seja o responsável por atualizar o field, não sendo necessário passar o field

//    Log.d(TAG, "offsetX: $offsetX - offsetY: $offsetY")
    Box(
        content  = { content() },
        modifier = modifier
            .offset {
                IntOffset(
                    x,
                    y,
                )
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDragField(
                        dragAmount.x.roundToInt(),
                        dragAmount.y.roundToInt()
                    )
                }
            }
            .pointerInput(Unit) {
                detectTapGestures {
                    onTapField()
                }
            },
    )
}
