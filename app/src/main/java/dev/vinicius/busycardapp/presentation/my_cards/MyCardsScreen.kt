package dev.vinicius.busycardapp.presentation.my_cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.core.presentation.CardsListing
import dev.vinicius.busycardapp.presentation.destinations.CardEditingScreenDestination
import dev.vinicius.busycardapp.presentation.destinations.CardDetailScreenDestination


// Currently being used to check every card
@RootNavGraph
@Destination
@Composable
fun MyCardsScreen(
    navigator: DestinationsNavigator,
    viewModel: MyCardsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var tabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.navigate(CardEditingScreenDestination(id = null))
                }
            ) {
                Icon(Icons.Filled.AddCard, contentDescription = "")
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            TabRow(selectedTabIndex = tabIndex) {
                Tab(
                    selected = tabIndex == 0,
                    onClick = { tabIndex = 0 }
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Meus Cartões"
                    )
                }
                Tab(
                    selected = tabIndex == 1,
                    onClick = { tabIndex = 1 }
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Rascunho")
                }
            }

            when (tabIndex) {
                0 -> MyCardsScreen(
                    modifier = Modifier.padding(it),
                    navigator = navigator,
                    state = state,
                    viewModel = viewModel
                )

                1 -> DraftCardsScreen(
                    modifier = Modifier.padding(it),
                    navigator = navigator,
                    state = state,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun MyCardsScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    state: MyCardsState,
    viewModel: MyCardsViewModel,

    ) {
    if (!state.isMyCardsLoading) {
        CardsListing(
            onClickItemCard = { id ->
                navigator.navigate(CardDetailScreenDestination(id = id))
            },
            cards = state.myCards,
            searchQuery = state.searchQuery,
            onSearchQueryChange = { query ->
                viewModel.onEvent(MyCardsEvent.OnSearchQueryChange(query))
            }
        )
    } else {
        Text("Loading")
    }

}

@Composable
fun DraftCardsScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    state: MyCardsState,
    viewModel: MyCardsViewModel,
) {
    if (!state.isDraftCardsLoading) {
        CardsListing(
            onClickItemCard = { id ->
                navigator.navigate(CardDetailScreenDestination(id = id))
            },
            cards = state.draftCards,
            searchQuery = state.searchQuery,
            onSearchQueryChange = { query ->
                viewModel.onEvent(MyCardsEvent.OnSearchQueryChange(query))
            }
        )
    } else {
        Text(stringResource(R.string.txt_loading))
    }
}