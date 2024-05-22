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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    ){
        Column(
            modifier = Modifier.padding(it)
        ) {
            Text(
                text = "Meus Cartões",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (!state.isMyCardsLoading) {
                CardsListing(
                    modifier = Modifier.padding(it),
                    onClickItemCard = { id ->
                        navigator.navigate(CardDetailScreenDestination(id = id))
                    },
                    cards = state.myCards
                )
            } else {
                Text("Loading")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Cartões em Rascunho",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (!state.isDraftCardsLoading) {
                CardsListing(
                    modifier = Modifier.padding(it),
                    onClickItemCard = { id ->
                        navigator.navigate(CardDetailScreenDestination(id = id))
                    },
                    cards = state.draftCards
                )
            } else {
                Text(stringResource(R.string.txt_loading))
            }
        }
    }
}
