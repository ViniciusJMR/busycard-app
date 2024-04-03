package dev.vinicius.busycardapp.presentation.card_creation.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.presentation.card_creation.CardCreationEvent
import dev.vinicius.busycardapp.presentation.card_creation.CardCreationState
import dev.vinicius.busycardapp.presentation.card_creation.component.DraggableFieldComponent

// TODO: Refatorar para não precisar receber o evento
@Composable
fun ShowCardSection(
    modifier: Modifier = Modifier,
    event: (CardCreationEvent) -> Unit,
    state: CardCreationState,
) {
    CardSurface(
        modifier,
        fields = state.cardFields,
        onDragField = {
            event(CardCreationEvent.CardEvent.OnSelectField(it))
        },
        onClickField = {
            event(CardCreationEvent.ModalEvent.OnShowBottomSheet(it))
        }
    )

}

@Composable
fun CardSurface(
    modifier: Modifier = Modifier,
    fields: List<Field>,
    onDragField: (Field) -> Unit,
    onClickField: (Field) -> Unit,
) {
    Surface(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .background(color = Color.DarkGray),
        ) {
            fields.forEach { field ->
                DraggableFieldComponent(
                    modifier = modifier,
                    field = field,
                    onDragField = onDragField,
                    onTapField = {
                        onClickField(it)
                    }
                ) {
                    when (field) {
                        is Field.AddressField -> TODO()
                        is Field.ImageField -> TODO()
                        is Field.TextField -> {
                            Text(
                                modifier = modifier,
                                text = field.value,
                            )
                        }

                    }
                }
            }
        }
    }
}
