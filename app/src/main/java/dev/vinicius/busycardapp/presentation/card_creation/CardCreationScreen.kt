package dev.vinicius.busycardapp.presentation.card_creation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.presentation.card_creation.section.OptionsSection
import dev.vinicius.busycardapp.presentation.card_creation.section.ShowCardSection
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun CardCreationScreen(
    navigator: DestinationsNavigator,
    viewModel: CardCreationViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState()

    LaunchedEffect(effect) {
        effect?.let {
            when(it) {
                CardCreationEffect.ClosePage -> {
                    navigator.navigateUp()
                }
            }
            viewModel.resetEffect()
        }


    }

    val event = viewModel::onEvent

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Card Creation") },
                actions = {
                    IconButton(onClick = { event(CardCreationEvent.CardEvent.OnSaveCard) }) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = stringResource(R.string.desc_save_current_card)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Field") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                onClick = { event(CardCreationEvent.ModalEvent.OnShowBottomSheet()) }
            )
        }
    ) { it ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            ShowCardSection(
                event = event,
                state = state
            )
            OptionsSection(
                onAddField = { fieldType -> event(CardCreationEvent.CardEvent.OnAddField(fieldType)) },
                onChangeField = { fieldEvent -> event(fieldEvent) },
                onDismissModalSheet = { event(CardCreationEvent.ModalEvent.OnDismissModalSheet) },
                onShowDialog = { event(CardCreationEvent.DialogEvent.OnShowTextTypeDialog) },
                currentlySelectedField = state.currentlySelectedField,
                showBottomSheet = state.showBottomSheet
            )
        }
    }
}

@Preview
@Composable
private fun ScreenPreview() {
    val state = CardCreationState()
    BusyCardAppTheme {
        //CardCreationScreen()
    }
}