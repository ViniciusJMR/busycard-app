package dev.vinicius.busycardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.FieldType
import dev.vinicius.busycardapp.presentation.card_creation.CardCreationEvent
import dev.vinicius.busycardapp.presentation.card_creation.CardCreationState
import dev.vinicius.busycardapp.presentation.card_creation.CardCreationViewModel
import dev.vinicius.busycardapp.presentation.card_creation.component.DraggableFieldComponent
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusyCardAppTheme {
                val viewModel = hiltViewModel<CardCreationViewModel>()
                val state by viewModel.state.collectAsState()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen(state = state, event = viewModel::onEvent)
                }
            }
        }
    }
}


@Composable
fun SelectableField(
    modifier: Modifier = Modifier,
    text: String,
    onClick: (FieldType) -> Unit,
    fieldType: FieldType,
) {

    TextButton(
        modifier = modifier,
        onClick = { onClick(fieldType) }
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = text,
        )
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
            SelectableField(
                modifier = Modifier.weight(1f),
                text = "Texto",
                onClick = onClickToAdd,
                fieldType = FieldType.TEXT,
            )
            SelectableField(
                modifier = Modifier.weight(1f),
                text = "Telefone",
                onClick = onClickToAdd,
                fieldType = FieldType.PHONE,
            )
        }

        Row(
            modifier = Modifier.padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SelectableField(
                modifier = Modifier.weight(1f),
                text = "Email",
                onClick = onClickToAdd,
                fieldType = FieldType.EMAIL,
            )
            SelectableField(
                modifier = Modifier.weight(1f),
                text = "Endereço",
                onClick = onClickToAdd,
                fieldType = FieldType.ADDRESS,
            )
        }

        Row(
            modifier = Modifier.padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SelectableField(
                modifier = Modifier.weight(1f),
                text = "Imagem", onClick = onClickToAdd, fieldType = FieldType.IMAGE
            )
        }
    }
}

@Composable
fun CardSurface(
    modifier: Modifier = Modifier,
    fields: List<Field>,
    onDragField: (Field) -> Unit,
) {
    Surface(
        modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
            .background(color = Color.LightGray),
        shape = RoundedCornerShape(5.dp)
    ) {
        Box(
            modifier = Modifier.height(200.dp)
        ) {
            fields.forEach { field ->
                HandleFields(
                    field = field,
                    onDragField = onDragField
                )
            }
        }
    }
}

@Composable
fun HandleFields(
    modifier: Modifier = Modifier,
    field: Field,
    onDragField: (Field) -> Unit,
) {
    DraggableFieldComponent(field = field, onDragField = onDragField) {
        when (field) {
            is Field.AddressField -> TODO()
            is Field.ImageField -> TODO()
            is Field.TextField -> TextField(field = field)
        }
    }
}

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    field: Field.TextField
) {
    Text(
        modifier = modifier,
        text = field.value,
    )
}

@Composable
fun ShowCardSection(
    modifier: Modifier = Modifier,
    event: (CardCreationEvent) -> Unit,
    state: CardCreationState,
) {

    CardSurface(
        fields = state.cardFields,
        onDragField = {
            event(CardCreationEvent.CardEvent.OnSelectField(it))
        }
    )

}


@Composable
fun TextFieldMenu(
    modifier: Modifier = Modifier,
    onChangeText: (CardCreationEvent.FieldEvent) -> Unit,
    field: Field.TextField,
) {
    var fieldValue by remember { mutableStateOf(field.value) }
    Column {
        OutlinedTextField(
            value = fieldValue,
            onValueChange = {
                fieldValue = it
                field.value = it
            }
        )
        Button(
            onClick = {
                onChangeText(CardCreationEvent.FieldEvent.OnTextFieldChange(field))
            }
        ) {
            Text("Confirm")
        }
    }
}

@Composable
fun FieldInfoMenu(
    modifier: Modifier = Modifier,
    field: Field,
    onChangeField: (CardCreationEvent.FieldEvent) -> Unit
) {
    when(field){
        is Field.AddressField -> TODO()
        is Field.ImageField -> TODO()
        is Field.TextField -> TextFieldMenu(onChangeText = onChangeField , field = field)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsSection(
    modifier: Modifier = Modifier,
    onAddField: (FieldType) -> Unit,
    onChangeField: (CardCreationEvent.FieldEvent) -> Unit,
    onDismissModalSheet: () -> Unit,
    currentlySelectedField: Field?,
    showBottomSheet: Boolean,
) {
    val sheetState = rememberModalBottomSheetState()
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissModalSheet,
            sheetState = sheetState
        ) {
            if (currentlySelectedField != null) {
                FieldInfoMenu(
                    field = currentlySelectedField,
                    onChangeField = onChangeField
                )
            } else {
                SelectFieldMenu(
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClickToAdd = onAddField
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OptionsSectionPreview() {
    BusyCardAppTheme {
        var currentlySelectedField by remember { mutableStateOf<Field?>(null) }
        if (currentlySelectedField != null) {
            FieldInfoMenu(field = currentlySelectedField!!, onChangeField = {})
        } else {
            SelectFieldMenu(
                modifier = Modifier.padding(bottom = 8.dp),
                onClickToAdd = {currentlySelectedField = Field.TextField()}
            )
        }
    }
}


@Composable
fun Screen(
    modifier: Modifier = Modifier,
    state: CardCreationState,
    event: (CardCreationEvent) -> Unit,
) {
    val TAG = "CardCreationScreen"
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Field") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                onClick = { event(CardCreationEvent.ModalEvent.OnShowBottomSheet()) }
            )
        }
    ) {
        Box(modifier.padding(it)) {
            ShowCardSection(
                event = event,
                state = state
            )
            OptionsSection(
                onAddField = { fieldType -> event(CardCreationEvent.CardEvent.OnAddField(fieldType)) },
                onChangeField = {fieldEvent -> event(fieldEvent)},
                onDismissModalSheet = { event(CardCreationEvent.ModalEvent.OnDismissModalSheet) },
                currentlySelectedField = state.currentlySelectedField,
                showBottomSheet = state.showBottomSheet
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectFieldMenuPreview() {
    val list: MutableList<Field> = listOf(
        Field.TextField(offsetX = 14f, offsetY = 200f, value = "Pinto pinto"),
        Field.TextField(offsetX = 0f, offsetY = 0f, value = "Pinto pinto"),
    ).toMutableList()

    BusyCardAppTheme {
    }
}