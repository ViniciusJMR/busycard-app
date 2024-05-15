package dev.vinicius.busycardapp.presentation.card_editing.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme


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
    onConfirmation: (String, Uri?) -> Unit,
    onDismiss: () -> Unit,
    cardName: String,
    cardImageUri: Uri?,
) {
    var newCardName by remember { mutableStateOf(cardName) }
    val imageUri = rememberSaveable {
        mutableStateOf(cardImageUri)
    }
    val painter = rememberAsyncImagePainter(
        imageUri.value?.toString() ?: R.drawable.outline_image_24
    )
    FullScreenDialog(onDismissRequest = onDismiss) {
        Column {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape) // TODO: Change to param
                    .size(150.dp)
            )
            LauncherForActivityResultComponent(
                onLauncherResult = {
                    imageUri.value = it
                }
            ) { launcher ->
                IconButton(onClick = { launcher.launch("image/*") }) {
                    Icon(
                        Icons.Outlined.PhotoCamera,
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))

            OutlinedTextField(value = newCardName, onValueChange = { newCardName = it })
            Spacer(modifier = Modifier.padding(8.dp))


            Button(
                onClick = {
                    onConfirmation(newCardName, imageUri.value)
                }
            ) {
                Text(stringResource(R.string.txt_label_confirm))
            }
        }
    }
}


@Preview
@Composable
private fun CardInfoPreview() {
   BusyCardAppTheme {
//       CardImage(modifier = Modifier.fillMaxSize())
   }
}