package dev.vinicius.busycardapp.presentation.card_info

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun CardInfoScreen(
    modifier: Modifier = Modifier,
    viewModel: CardInfoViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    Column {
        Text("Card Info Screen")
    }
}