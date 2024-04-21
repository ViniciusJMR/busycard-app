package dev.vinicius.busycardapp.presentation.card_creation.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.FieldType
import dev.vinicius.busycardapp.domain.model.card.TextType
import dev.vinicius.busycardapp.presentation.card_creation.CardCreationEvent
import dev.vinicius.busycardapp.presentation.card_creation.component.DefaultDialog
import dev.vinicius.busycardapp.presentation.card_creation.component.RadioOptions
import dev.vinicius.busycardapp.presentation.card_creation.component.SelectableOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsSection(
    modifier: Modifier = Modifier,
    onAddField: (FieldType) -> Unit,
    onChangeField: (CardCreationEvent.FieldEvent) -> Unit,
    onDismissModalSheet: () -> Unit,
    onShowDialog: () -> Unit,
    onMainContactChange: (Field.TextField) -> Unit,
    mainContactField: Field?,
    currentlySelectedField: Field?,
    showBottomSheet: Boolean,
) {
    val sheetState = rememberModalBottomSheetState()
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.padding(bottom = 16.dp),
            onDismissRequest = onDismissModalSheet,
            sheetState = sheetState
        ) {
            if (currentlySelectedField != null) {
                FieldInfoMenu(
                    field = currentlySelectedField,
                    onChangeField = onChangeField,
                    isMainContact = currentlySelectedField === mainContactField,
                    onMainContactChange = { textField -> onMainContactChange(textField) },
                    onShowDialog = onShowDialog
                )
            } else {
                SelectFieldMenu(
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClickToAdd = onAddField
                )
            }
            // Sem o Spacer os botões ficam atras do botões de voltar/home padrão do celular
            Spacer(Modifier.size(48.dp))
        }
    }
}

@Composable
fun FieldInfoMenu(
    modifier: Modifier = Modifier,
    onChangeField: (CardCreationEvent.FieldEvent) -> Unit, // TODO: Mudar nome para onFieldChanged?,
    onShowDialog: () -> Unit,
    isMainContact: Boolean,
    onMainContactChange: (Field.TextField) -> Unit,
    field: Field,
) {
    Column {
        when (field) {
            is Field.AddressField -> TODO()
            is Field.ImageField -> TODO()
            is Field.TextField -> {
                TextButton(onClick = { onMainContactChange(field) }) {
                    val text = if (!isMainContact)
                        stringResource(R.string.txt_select_as_main_ctc)
                    else
                        stringResource(R.string.txt_unselect_as_main_contact)
                    Text(text)
                }
                Spacer(Modifier.padding(8.dp))
                TextFieldMenu(
                    onChangeText = onChangeField,
                    onChangeTextType = onShowDialog,
                    field = field
                )
            }
        }
    }
}

@Composable
fun TextFieldMenu(
    modifier: Modifier = Modifier,
    onChangeText: (CardCreationEvent.FieldEvent) -> Unit,
    onChangeTextType: () -> Unit,
    field: Field.TextField,
) {
    var fieldValue by remember { mutableStateOf(field.value) }
    var fieldType by remember { mutableStateOf(field.textType) }
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
    ) {
        if (showDialog) {
            DefaultDialog(
                onDismissRequest = { showDialog = false },
                onConfirmation = { showDialog = false },
                dialogTitle = "Choose Text Type"
            ) {
                RadioOptions(
                    onOptionSelected = { fieldType = it },
                    options = TextType.entries,
                    currentlySelectedOption = fieldType,
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = fieldValue,
            onValueChange = {
                fieldValue = it
            }
        )
        Spacer(Modifier.size(4.dp))
        TextButton(
            onClick = { showDialog = true },
        ) {
            val textValue = when (fieldType) {
                TextType.TEXT -> stringResource(R.string.txt_texttype_text)
                TextType.PHONE -> stringResource(R.string.txt_texttype_phone)
                TextType.EMAIL -> stringResource(R.string.txt_texttype_email)
            }
            Text(textValue)
        }
        Spacer(Modifier.size(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            onClick = {
                onChangeText(
                    CardCreationEvent.FieldEvent.OnTextFieldChange(
                        value = fieldValue,
                        textType = fieldType,
                    )
                )
            },
        ) {
            Text("Confirm")
        }
    }
}

@Composable
fun SelectFieldMenu(
    modifier: Modifier = Modifier,
    onClickToAdd: (FieldType) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Row(
            modifier = Modifier.padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SelectableOption(
                modifier = Modifier.weight(1f),
                text = "Texto",
                onClick = { onClickToAdd(FieldType.TEXT) },
            )
            SelectableOption(
                modifier = Modifier.weight(1f),
                text = "Telefone",
                onClick = { onClickToAdd(FieldType.PHONE) },
            )
        }

        Row(
            modifier = Modifier.padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SelectableOption(
                modifier = Modifier.weight(1f),
                text = "Email",
                onClick = { onClickToAdd(FieldType.EMAIL) },
            )
            SelectableOption(
                modifier = Modifier.weight(1f),
                text = "Endereço",
                onClick = { onClickToAdd(FieldType.ADDRESS) },
            )
        }

        Row(
            modifier = Modifier.padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SelectableOption(
                modifier = Modifier.weight(1f),
                text = "Imagem",
                onClick = { onClickToAdd(FieldType.IMAGE) }
            )
        }
    }
}
