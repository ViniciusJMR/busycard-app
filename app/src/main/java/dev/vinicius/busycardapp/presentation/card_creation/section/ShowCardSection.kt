package dev.vinicius.busycardapp.presentation.card_creation.section

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.presentation.card_creation.CardCreationEvent
import dev.vinicius.busycardapp.presentation.card_creation.CardCreationState
import dev.vinicius.busycardapp.presentation.card_creation.component.DraggableFieldComponent
import kotlin.math.log

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
                    field = field,
                    onDragField = onDragField,
                    onTapField = {
                        onClickField(it)
                    }
                ) {
                    when (field) {
                        is Field.AddressField -> TODO()
                        is Field.ImageField -> {
                            ImageFieldShow(
                                uri = field.image.uri,
                                size = field.size.toInt(),
                            )
                            Log.d(TAG, "field: $field")
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