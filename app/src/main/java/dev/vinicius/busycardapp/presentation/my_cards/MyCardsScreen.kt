package dev.vinicius.busycardapp.presentation.my_cards

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.vinicius.busycardapp.core.presentation.CardsListing
import dev.vinicius.busycardapp.presentation.destinations.CardCreationScreenDestination
import dev.vinicius.busycardapp.presentation.destinations.CardInfoScreenDestination


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
                    navigator.navigate(CardCreationScreenDestination)
                }
            ) {
                Icon(Icons.Filled.AddCard, contentDescription = "")
            }
        }
    ){
        if (!state.isLoading) {
            CardsListing(
                modifier = Modifier.padding(it),
                onClickItemCard = { id ->
                    navigator.navigate(CardInfoScreenDestination(id = id))
                },
                cards = state.cards
            )
        } else {
            Text("Loading")
        }
    }
}
