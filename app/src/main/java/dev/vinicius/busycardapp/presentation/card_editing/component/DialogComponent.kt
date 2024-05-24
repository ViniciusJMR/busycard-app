package dev.vinicius.busycardapp.presentation.card_editing.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import dev.vinicius.busycardapp.core.presentation.component.DialogComponent
import dev.vinicius.busycardapp.domain.model.card.enums.CardColor
import dev.vinicius.busycardapp.domain.model.card.enums.CardSize
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardInfoDialog(
    modifier: Modifier = Modifier,
    onConfirmation: (String, Uri?, CardSize, CardColor) -> Unit,
    onDismiss: () -> Unit,
    cardName: String,
    cardImageUri: Uri?,
    cardSize: CardSize,
    cardColor: CardColor,
) {
    val sheetState = rememberModalBottomSheetState(true)

    var newCardName by remember { mutableStateOf(cardName) }
    var newCardSize by remember { mutableStateOf(cardSize) }
    var newCardColor by remember { mutableStateOf(cardColor) }

    var showSizeDialog by remember { mutableStateOf(false) }
    var showColorDialog by remember { mutableStateOf(false) }


    val imageUri = rememberSaveable {
        mutableStateOf(cardImageUri)
    }

    val painter = rememberAsyncImagePainter(
        imageUri.value?.toString() ?: R.drawable.outline_image_24
    )


    if (showSizeDialog) {
        DialogComponent(
            onDismiss = { showSizeDialog = false },
            onConfirm = { showSizeDialog = false },
        ) {
            GRadioOptions(
                onOptionSelected = { newCardSize = it },
                stringsForOptions = {
                    when(it) {
                        CardSize.SMALL  -> "Pequeno"
                        CardSize.MEDIUM -> "Médio"
                        CardSize.LARGE  -> "Grande"
                    }
                },
                options = CardSize.entries,
                currentlySelectedOption = newCardSize
            )
        }
    }

    if (showColorDialog) {
        DialogComponent(
            onDismiss = { showColorDialog = false },
            onConfirm = { showColorDialog = false },
        ) {
            GRadioOptions(
                onOptionSelected = { newCardColor = it },
                stringsForOptions = {
                    when(it) {
                        CardColor.Black -> "Preto"
                        CardColor.DarkGray -> "Cinza escuro"
                        CardColor.LightGray -> "Cinza claro"
                        CardColor.Red -> "Vermelho"
                        CardColor.Green -> "Verde"
                        CardColor.Blue -> "Azul"
                        CardColor.Yellow -> "Amarelo"
                        CardColor.Cyan -> "Ciano"
                        CardColor.Gray -> "Cinza"
                    }
                },
                options = CardColor.entries,
                currentlySelectedOption = newCardColor
            )
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape) // TODO: Change to param
                .size(150.dp)
        )
        LauncherForActivityResultComponent(
            onLauncherResult = {
                imageUri.value = it
            }
        ) { launcher ->
            IconButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { launcher.launch("image/*") }
            ) {
                Icon(
                    Icons.Outlined.PhotoCamera,
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            value = newCardName,
            onValueChange = { newCardName = it },
            label = { Text("Nome do Cartão") },
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Tamanho do cartão: ")
            TextButton(onClick = { showSizeDialog = true }) {
                Text(when(newCardSize) {
                    CardSize.SMALL  -> "Pequeno"
                    CardSize.MEDIUM -> "Médio"
                    CardSize.LARGE  -> "Grande"
                })
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Cor do cartão: ")
            TextButton(onClick = { showColorDialog = true }) {
                Text(when(newCardColor) {
                        CardColor.Black -> "Preto"
                        CardColor.DarkGray -> "Cinza escuro"
                        CardColor.LightGray -> "Cinza claro"
                        CardColor.Red -> "Vermelho"
                        CardColor.Green -> "Verde"
                        CardColor.Blue -> "Azul"
                        CardColor.Yellow -> "Amarelo"
                        CardColor.Cyan -> "Ciano"
                        CardColor.Gray -> "Cinza"
                })
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End),
            onClick = {
                onConfirmation(newCardName, imageUri.value, newCardSize, newCardColor)
            }
        ) {
            Text(stringResource(R.string.txt_label_confirm))
        }
        // Sem o Spacer os botões ficam atras do botões de voltar/home padrão do celular
        Spacer(Modifier.size(48.dp))
    }
}


@Preview
@Composable
private fun CardInfoPreview() {
   BusyCardAppTheme {
//       CardImage(modifier = Modifier.fillMaxSize())
   }
}