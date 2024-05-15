package dev.vinicius.busycardapp.presentation.card_editing.section

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.core.checkForPermission
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.FieldType
import dev.vinicius.busycardapp.domain.model.card.TextType
import dev.vinicius.busycardapp.presentation.card_editing.CardEditingEvent
import dev.vinicius.busycardapp.presentation.card_editing.component.DefaultDialog
import dev.vinicius.busycardapp.presentation.card_editing.component.FullScreenDialog
import dev.vinicius.busycardapp.presentation.card_editing.component.GoogleMapComponent
import dev.vinicius.busycardapp.presentation.card_editing.component.LauncherForActivityResultComponent
import dev.vinicius.busycardapp.presentation.card_editing.component.RadioOptions
import dev.vinicius.busycardapp.presentation.card_editing.component.SelectableOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsSection(
    modifier: Modifier = Modifier,
    onAddField: (FieldType) -> Unit,
    onChangeField: (CardEditingEvent.FieldEvent) -> Unit,
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
    onChangeField: (CardEditingEvent.FieldEvent) -> Unit, // TODO: Mudar nome para onFieldChanged?,
    onShowDialog: () -> Unit,
    isMainContact: Boolean,
    onMainContactChange: (Field.TextField) -> Unit,
    field: Field,
) {
    Column {
        when (field) {
            is Field.AddressField -> {
                AddressFieldMenu(
                    onChangeAddress = onChangeField,
                    field = field
                )
            }
            is Field.ImageField -> {
                ImageFieldMenu(onChangeImage = onChangeField, field = field)
            }
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
    onChangeText: (CardEditingEvent.FieldEvent) -> Unit,
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
                    CardEditingEvent.FieldEvent.OnTextFieldChange(
                        value = fieldValue,
                        textType = fieldType,
                    )
                )
            },
        ) {
            Text(stringResource(R.string.txt_label_confirm))
        }
    }
}

@Composable
fun ImageFieldMenu(
    modifier: Modifier = Modifier,
    onChangeImage: (CardEditingEvent.FieldEvent) -> Unit,
    field: Field.ImageField,
) {

    val imageUri = rememberSaveable {
        mutableStateOf(field.image.uri)
    }

    var size by remember {
        mutableIntStateOf(field.size)
    }

    val painter = rememberAsyncImagePainter(
        imageUri.value?.toString() ?: R.drawable.outline_image_24
    )

    Column {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape) // TODO: Change to param
                .size(size.dp)
        )
        LauncherForActivityResultComponent(
            onLauncherResult = {
                imageUri.value = it
            }
        ) { launcher ->
            IconButton(onClick = { launcher.launch("image/*") }) {
                Icon(
                    Icons.Outlined.Upload,
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { size-- }) {
                Icon(Icons.Outlined.ChevronLeft, contentDescription = null)
            }
            Text(text = stringResource(R.string.label_change_image_size))
            IconButton(onClick = { size++ }) {
                Icon(Icons.Outlined.ChevronRight, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            onClick = {
                onChangeImage(
                    CardEditingEvent.FieldEvent.OnImageFieldChange(
                        size = size,
                        uri = imageUri.value,
                    )
                )
            },
        ) {
            Text(stringResource(R.string.txt_label_confirm))
        }
    }
}

@Composable
fun AddressFieldMenu(
    modifier: Modifier = Modifier,
    onChangeAddress: (CardEditingEvent.FieldEvent) -> Unit,
    field: Field.AddressField,
) {
    val TAG = "AddressFieldMenu"
    val context = LocalContext.current

    var textLocalization by remember { mutableStateOf(field.textLocalization) }
    var localization by remember { mutableStateOf(field.localization) }
    var showMap by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        permissions.entries.forEach {
            if (!it.value) {
                return@forEach
            }
            showMap = true
        }
    }

    if (showMap) {
        FullScreenDialog(onDismissRequest = { showMap = false }) {
            GoogleMapComponent(
                onMapClick = { latLng ->
                    localization = latLng
                    Log.d("AddressFieldMenu", "localization: $localization")
                },
                onConfirm = {
                    showMap = false
                },
                onClearMarkers = {
                    localization = null
                },
                latLng = localization
            )
        }
    }

    Column {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            value = textLocalization,
            onValueChange = {
                textLocalization = it
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        TextButton(onClick = {
            if (!checkForPermission(context)) {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                showMap = true
            }
        }) {
            Text(stringResource(R.string.txt_label_address_localization))
        }
        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            onClick = {
                Log.d(TAG, "AddressFieldMenu: Button clicked - localization: $localization")
                onChangeAddress(
                    CardEditingEvent.FieldEvent.OnAddressFieldChange(
                        textLocalization = textLocalization,
                        localization = localization,
                    )
                )
            }
        ) {
            Text(stringResource(R.string.txt_label_confirm))
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

