package dev.vinicius.busycardapp.presentation.card_detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme


@RootNavGraph
@Destination
@Composable
fun CardInfoScreen(
    modifier: Modifier = Modifier,
    viewModel: CardDetailViewModel = hiltViewModel(),
    id: String,
) {
    val state by viewModel.state.collectAsState()
    val TAG = "CardInfoScreen"

    Log.d(TAG, "CardInfoScreen: isLoading: ${state.isLoading}")
    Box (
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (!state.isLoading) {
            Log.d(TAG, "CardInfoScreen: fields: ${state.fields}")
            CardRender(fields = state.fields, size = 200)
        } else {
            Text(text = "Loading")
        }

    }
}

@Composable
fun CardRender(
    modifier: Modifier = Modifier,
    fields: List<Field>,
    size: Int
) {
    Surface (
        modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ){
        Box (
            Modifier
                .height(size.dp)
                .background(color = Color.DarkGray)
        ){
            fields.forEach { field ->
                Box (
                    Modifier.offset {
                        IntOffset(
                            field.offsetX,
                            field.offsetY,
                        )
                    }
                ){
                    when (field) {
                        is Field.AddressField -> CardInfoAddressField(field = field)
                        is Field.ImageField -> CardInfoImageField(field = field)
                        is Field.TextField -> CardInfoTextField(field = field)
                    }
                }
            }
        }
    }
}

@Composable
fun CardInfoTextField(
    modifier: Modifier = Modifier,
    field: Field.TextField,
) {
    Text( field.value )
    // TODO: Different onClick depending on TextType
}

@Composable
fun CardInfoImageField(
    modifier: Modifier = Modifier,
    field: Field.ImageField,
) {
    Box( contentAlignment = Alignment.Center ) {
        // Only used to signalize to the user there's a image there
        // TODO: Use onState from AsyncImage
        CircularProgressIndicator()
        AsyncImage(
            model = field.image.path,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(CircleShape) // TODO: Change to param
                .size(field.size.dp)
        )
    }
}

@Composable
fun CardInfoAddressField(
    modifier: Modifier = Modifier,
    field: Field.AddressField,
) {
    val TAG = "CardInfoAddressField"
    Log.d(TAG, "field: ${field.localization}")
    Row {
        Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null)
        Text(text = field.textLocalization)
    }
}


@Preview
@Composable
private fun CardRenderPreview() {
    val a = listOf(
        Field.TextField(value = "Print"),
//        Field.TextField(value = "Print", offsetX = 23.0F, offsetY = 100F),
//        Field.TextField(value = "Print", offsetX = 223.0F, offsetY = 400F),
    )

    BusyCardAppTheme {
        CardRender(fields = a, size = 200)
    }
}

