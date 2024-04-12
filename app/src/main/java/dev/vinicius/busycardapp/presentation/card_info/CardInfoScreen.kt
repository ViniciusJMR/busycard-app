package dev.vinicius.busycardapp.presentation.card_info

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme
import kotlinx.coroutines.flow.collect
import kotlin.math.log
import kotlin.math.roundToInt


@Destination
@Composable
fun CardInfoScreen(
    modifier: Modifier = Modifier,
    viewModel: CardInfoViewModel = hiltViewModel(),
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
                            field.offsetX.roundToInt(),
                            field.offsetY.roundToInt()
                        )
                    }
                ){
                    when (field) {
                        is Field.AddressField -> TODO()
                        is Field.ImageField -> TODO()
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

@Preview
@Composable
private fun CardRenderPreview() {
    val a = listOf(
        Field.TextField(value = "Print"),
        Field.TextField(value = "Print", offsetX = 23.0F, offsetY = 100F),
        Field.TextField(value = "Print", offsetX = 223.0F, offsetY = 400F),
    )

    BusyCardAppTheme {
        CardRender(fields = a, size = 200)
    }
}

