package dev.vinicius.busycardapp.presentation.my_cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.presentation.card_creation.CardCreationEvent
import dev.vinicius.busycardapp.presentation.card_info.CardInfoScreen
import dev.vinicius.busycardapp.presentation.destinations.CardCreationScreenDestination
import dev.vinicius.busycardapp.presentation.destinations.CardInfoScreenDestination
import dev.vinicius.busycardapp.presentation.shared_cards.CardItem
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme


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
            MyCardsListing(
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

@Composable
fun MyCardsListing(
    modifier: Modifier = Modifier,
    onClickItemCard: (String) -> Unit,
    cards: List<Card>
) {

    LazyColumn (
        modifier = modifier
    ) {
        items(
            items = cards,
            key = { card -> card.id!! }
        ) { card ->
            MyCardItem(
                name = card.name,
                mainContact = "place@holder", // TODO: Change this when adding main Contact,
                imageUrl = "",
                onClick = { onClickItemCard(card.id!!) }
            )
        }
    }
}

@Preview
@Composable
private fun MyCardsListingPreview() {
    val a = List(30) { i -> Card(id = i.toString(), name = "Card #$i", owner = "", fields = emptyList()) }
    BusyCardAppTheme {
        //MyCardsListing(cards = a)
    }
}

@Composable
fun MyCardItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    name: String,
    mainContact: String,
    imageUrl: String,
) {
    Surface (
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp),
        onClick = onClick
    ){
        Row (
        ){
            Icon(
                modifier = Modifier.fillMaxHeight(),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null
            )
            Column {
                Text(
                    text = name,
                )
                Text(
                    text = mainContact,
                    style = TextStyle(
                        fontWeight = FontWeight.Light
                    )
                )
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
private fun CardItemPreview() {
    BusyCardAppTheme {
        CardItem(
            name = "Cartão do joão",
            mainContact = "place@holder.com",
            imageUrl = "",
        )
    }
}