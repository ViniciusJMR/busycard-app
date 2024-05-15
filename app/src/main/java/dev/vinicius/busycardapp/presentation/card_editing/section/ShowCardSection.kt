package dev.vinicius.busycardapp.presentation.card_editing.section

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.presentation.card_editing.CardEditingEvent
import dev.vinicius.busycardapp.presentation.card_editing.CardEditingState
import dev.vinicius.busycardapp.presentation.card_editing.component.DraggableFieldComponent
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme

// TODO: Refatorar para não precisar receber o evento
@Composable
fun ShowCardSection(
    modifier: Modifier = Modifier,
    event: (CardEditingEvent) -> Unit,
    state: CardEditingState,
) {
    CardSurface(
        modifier,
        fields = state.cardFields,
        onDragField = {
            event(CardEditingEvent.CardEvent.OnSelectField(it))
        },
        onClickField = {
            event(CardEditingEvent.ModalEvent.OnShowBottomSheet(it))
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
    val TAG = "CardSurface"
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
                    initialX = field.offsetX,
                    initialY = field.offsetY,
                    onDragField = { x, y ->
                        field.offsetX = x
                        field.offsetY = y
                        onDragField(field)
                    },
                    onTapField = {
                        onClickField(field)
                    }
                ) {
                    when (field) {
                        is Field.AddressField ->  {
                            AddressFieldShow(addressText = field.textLocalization)
                        }
                        is Field.ImageField -> {
                            ImageFieldShow(
                                uri = field.image.uri,
                                size = field.size,
                            )
                        }
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


@Composable
fun ImageFieldShow(
    modifier: Modifier = Modifier,
    uri: Uri?,
    size: Int,
) {
    val TAG = "ImageFieldShow"


    val painter = rememberAsyncImagePainter(
        uri?.toString() ?: R.drawable.outline_hide_image_24
    )

    Log.d(TAG, "fieldUri: $uri")
    Log.d(TAG, "painter value: ${uri?.toString() ?: R.drawable.outline_hide_image_24} ")

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape) // TODO: Change to param
            .size(size.dp)
    )
}

@Composable
fun AddressFieldShow(
    modifier: Modifier = Modifier,
    addressText: String,
) {
    val TAG = "AddressFieldShow"

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = "")
        Text(text = addressText)
    }
}

@Preview(showBackground = true)
@Composable
private fun AddressFieldShowPreview() {
    BusyCardAppTheme {
        AddressFieldShow(
            modifier = Modifier.padding(8.dp),
            addressText = "Rua do Amor, 123"
        )
    }
}