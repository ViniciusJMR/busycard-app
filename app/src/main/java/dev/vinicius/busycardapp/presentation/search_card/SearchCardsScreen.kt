package dev.vinicius.busycardapp.presentation.search_card

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.core.presentation.CardsListing
import dev.vinicius.busycardapp.presentation.destinations.CardDetailScreenDestination


// Currently being used to check every card
@Destination
@Composable
fun SearchCardsScreen(
    navigator: DestinationsNavigator,
    viewModel: SearchCardsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    // TODO: Change to Scarfold
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ){
        if (!state.isLoading) {
            CardsListing(
                modifier = Modifier.padding(it),
                onClickItemCard = { id ->
                    navigator.navigate(CardDetailScreenDestination(id = id))
                },
                cards = state.cards,
                searchQuery = state.searchQuery,
                onSearchQueryChange = { query ->
                    viewModel.onEvent(SearchCardsEvent.OnSearchQueryChange(query))
                },
                isRefreshing = state.isRefreshing,
                onRefresh = { viewModel.onEvent(SearchCardsEvent.Refresh) }
            )
        } else {
            Text(stringResource(R.string.txt_loading))
        }
    }
}
