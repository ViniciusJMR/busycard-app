package dev.vinicius.busycardapp.presentation.card_creation.component

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import dev.vinicius.busycardapp.domain.model.card.FieldType

@Composable
fun SelectableOption(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {

    TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = text,
        )
    }
}
