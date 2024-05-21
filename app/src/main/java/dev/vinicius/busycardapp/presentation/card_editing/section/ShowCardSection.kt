package dev.vinicius.busycardapp.presentation.card_editing.section

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.enums.FieldFont
import dev.vinicius.busycardapp.domain.model.card.enums.LocationIconPosition
import dev.vinicius.busycardapp.presentation.card_editing.CardEditingEvent
import dev.vinicius.busycardapp.presentation.card_editing.component.DraggableFieldComponent
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme
import kotlin.math.roundToInt

// TODO: Refatorar para não precisar receber o evento
@Composable
fun ShowCardSection(
    modifier: Modifier = Modifier,
    event: (CardEditingEvent) -> Unit,
    fields: List<Field>,
    updateUiState: Int,
) {
    /* Gambiarra falada no Viewmodel
     * Sem ela a UI não é atualizada e o field não é arrastado pela tela
     */
    Text(text = updateUiState.toString())
    CardSurface(
        modifier,
        fields = fields,
        onDragField = { field, offsetX, offsetY ->
            event(CardEditingEvent.CardEvent.OnDragField(field, offsetX, offsetY))
//              event(CardEditingEvent.CardEvent.OnSelectField(field))
        },
        onClickField = {
            Log.d("ShowCardSection", "OnClickField - ${it}")
            event(CardEditingEvent.ModalEvent.OnShowBottomSheet(it))
        }
    )

}

@Composable
fun CardSurface(
    modifier: Modifier = Modifier,
    fields: List<Field>,
    onDragField: (Field,Int, Int) -> Unit,
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
            Log.d(TAG, "CardSurface: fields: ${fields.map { "$it, "}}")
            fields.forEachIndexed { index, field ->
                DraggableFieldComponent(
                    x = fields[index].offsetX,
                    y = fields[index].offsetY,
                    onDragField = { x, y ->
                        onDragField(
                            fields[index],
                            fields[index].offsetX + x,
                            fields[index].offsetY + y
                        )
                    },
                    onTapField = {
                        onClickField(fields[index])
                    }

                ) {
                    when (field) {
                        is Field.AddressField ->  {
                            AddressFieldShow(
                                addressText = field.textLocalization,
                                font = field.font,
                                size = field.size,
                                iconSize = field.iconSize,
                                locationIconPosition = field.iconPosition,
                            )
                        }
                        is Field.ImageField -> {
                            ImageFieldShow(
                                uri = field.image.uri,
                                size = field.size,
                            )
                        }
                        is Field.TextField -> {
                            TextFieldShow(
                                modifier = modifier,
                                text = field.value,
                                font = field.font,
                                size = field.size,
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
    Log.d(TAG, "REcomposição Imagem")


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
    font: FieldFont,
    size: Int,
    iconSize: Int,
    locationIconPosition: LocationIconPosition
) {
    val TAG = "AddressFieldShow"
    Log.d(TAG, "REcomposiçaõ - valor do texto: $addressText")

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (locationIconPosition == LocationIconPosition.LEFT) {
            Icon(
                modifier = Modifier.size(iconSize.dp),
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "",
            )

        }
        Text(
            text = addressText,
            fontFamily = when(font) {
                FieldFont.DEFAULT -> FontFamily.Default
                FieldFont.SERIF -> FontFamily.Serif
                FieldFont.SANS_SERIF -> FontFamily.SansSerif
                FieldFont.MONOSPACE -> FontFamily.Monospace
                FieldFont.CURSIVE -> FontFamily.Cursive
            },
            fontSize = size.sp,
        )
        if (locationIconPosition == LocationIconPosition.RIGHT) {
            Icon(
                modifier = Modifier.size(iconSize.dp),
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "",
            )

        }
    }
}

@Composable
fun TextFieldShow(
    modifier: Modifier = Modifier,
    text: String,
    font: FieldFont,
    size: Int,
) {
    val TAG = "TextFieldShow"
    Log.d(TAG, "REcomposição - valor do texto: $text")

    Text(
        modifier = modifier,
        text = text,
        fontSize = size.sp,
        fontFamily = when(font) {
            FieldFont.DEFAULT -> FontFamily.Default
            FieldFont.SERIF -> FontFamily.Serif
            FieldFont.SANS_SERIF -> FontFamily.SansSerif
            FieldFont.MONOSPACE -> FontFamily.Monospace
            FieldFont.CURSIVE -> FontFamily.Cursive
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AddressFieldShowPreview() {
    BusyCardAppTheme {
//        AddressFieldShow(
//            modifier = Modifier.padding(8.dp),
//            addressText = "Rua do Amor, 123"
//        )
    }
}