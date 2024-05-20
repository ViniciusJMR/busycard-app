package dev.vinicius.busycardapp.presentation.card_editing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.presentation.card_detail.CardInfoEvent
import dev.vinicius.busycardapp.presentation.card_detail.component.DialogComponent
import dev.vinicius.busycardapp.presentation.card_editing.component.CardInfoDialog
import dev.vinicius.busycardapp.presentation.card_editing.section.OptionsSection
import dev.vinicius.busycardapp.presentation.card_editing.section.ShowCardSection
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun CardEditingScreen(
    navigator: DestinationsNavigator,
    viewModel: CardEditingViewModel = hiltViewModel(),
    id: String?
) {

    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState()
    val fields = viewModel.fields
    val updateUiState by viewModel.updateUiState.collectAsState()

    LaunchedEffect(effect) {
        effect?.let {
            when(it) {
                CardEditingEffect.ClosePage -> {
                    navigator.navigateUp()
                }
            }
            viewModel.resetEffect()
        }
    }

    val event = viewModel::onEvent

    if (state.showSaveDialog) {
        CardDetailSaveDialog(
            onConfirm = { event(CardEditingEvent.DialogEvent.OnDismissSaveDialog) },
            onDismiss = { event(CardEditingEvent.DialogEvent.OnDismissSaveDialog) },
            onClickSave = { event(CardEditingEvent.CardEvent.OnSaveEvent.OnSaveCard) },
            onClickSaveAsDraft = {
                event(CardEditingEvent.CardEvent.OnSaveEvent.OnSaveCardAsDraft)
            },
        )
    }

    if (state.showCardInfoDialog) {
        CardInfoDialog(
            onConfirmation = { name, uri ->
                event(CardEditingEvent.CardEvent.OnChangeCard.Info(name, uri))
            },
            onDismiss = { event(CardEditingEvent.DialogEvent.OnDismissCardInfoDialog) },
            cardName = state.cardName,
            cardImageUri = state.cardImageUri
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text( state.cardName.ifBlank { stringResource(R.string.txt_card_no_name) } )
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { event(CardEditingEvent.DialogEvent.OnShowSaveDialog) }) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = stringResource(R.string.desc_save_current_card)
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.Layers, contentDescription = "")
                    }
                    IconButton(onClick = { event(CardEditingEvent.DialogEvent.OnShowCardInfoDialog)}) {
                        Icon(Icons.Outlined.Settings, contentDescription = "")
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { event(CardEditingEvent.ModalEvent.OnShowBottomSheet()) },
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "")
                    }
                },
            )
        },
    ) { it ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            ShowCardSection(
                event = event,
                fields = fields,
                updateUiState = updateUiState,
            )
            OptionsSection(
                onAddField = { fieldType -> event(CardEditingEvent.CardEvent.OnAddField(fieldType)) },
                onChangeField = { fieldEvent -> event(fieldEvent) },
                onDismissModalSheet = { event(CardEditingEvent.ModalEvent.OnDismissModalSheet) },
                onShowDialog = { event(CardEditingEvent.DialogEvent.OnShowTextTypeDialog) },
                onMainContactChange = { event(CardEditingEvent.CardEvent.OnChangeCard.MainContact(it))},
                onDeleteField = { event(CardEditingEvent.CardEvent.OnDeleteField(it)) },
                currentlySelectedField = state.currentlySelectedField,
                mainContactField = state.mainContactField,
                showBottomSheet = state.showBottomSheet
            )
        }
    }
}

@Composable
fun CardDetailSaveDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onClickSave: () -> Unit,
    onClickSaveAsDraft: () -> Unit
) {
    DialogComponent(
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        confirmText = R.string.txt_close,
    ) {
        Column {
            TextButton( onClick = onClickSaveAsDraft ) {
                Text( stringResource( R.string.txt_save_as_draft) )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton( onClick = onClickSave ) {
                Text( stringResource(R.string.txt_save ) )
            }
        }
    }
}

@Preview
@Composable
private fun ScreenPreview() {
    val state = CardEditingState()
    BusyCardAppTheme {
        //CardCreationScreen()
    }
}