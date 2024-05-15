package dev.vinicius.busycardapp.presentation.card_editing.component


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.domain.model.card.TextType

@Composable
fun RadioOptions(
    modifier: Modifier = Modifier,
    onOptionSelected: (TextType) -> Unit,
    options: List<TextType>,
    currentlySelectedOption: TextType
) {
    var selectedOption by remember { mutableStateOf(currentlySelectedOption) }
// Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(Modifier.selectableGroup()) {
        options.forEach { text ->
            val textValue = when (text) {
                TextType.TEXT -> stringResource(R.string.txt_texttype_text)
                TextType.PHONE -> stringResource(R.string.txt_texttype_phone)
                TextType.EMAIL -> stringResource(R.string.txt_texttype_email)
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            selectedOption = text
                            onOptionSelected(text)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = textValue,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }

}