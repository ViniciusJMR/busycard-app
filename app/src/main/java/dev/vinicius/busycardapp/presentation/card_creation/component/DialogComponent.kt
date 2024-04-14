package dev.vinicius.busycardapp.presentation.card_creation.component

import android.app.Dialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.domain.model.card.TextType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    content: @Composable () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = content,
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun FullScreenDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismissRequest
    ) {
        Surface(modifier = Modifier.fillMaxSize()){
            content()
        }
    }
}

@Composable
fun CardInfoDialog(
    modifier: Modifier = Modifier,
    onConfirmation: (String) -> Unit,
    cardName: String,

) {
    var newCardName by remember { mutableStateOf(cardName) }
    FullScreenDialog(onDismissRequest = { /*TODO*/ }) {
        Column {
            OutlinedTextField(value = newCardName, onValueChange = { newCardName = it })

            Button(onClick = { onConfirmation(newCardName) }) {
                Text(stringResource(R.string.txt_label_confirm))
            }
        }
    }
}