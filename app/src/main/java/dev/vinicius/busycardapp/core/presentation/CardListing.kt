package dev.vinicius.busycardapp.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme

@Composable
fun CardsListing(
    modifier: Modifier = Modifier,
    onClickItemCard: (String) -> Unit,
    cards: List<Card>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
) {
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(text = "Search")
            },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        )
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.Center
        ) {
            items(
                items = cards,
                key = { card -> card.id!! }
            ) { card ->
                CardItem(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    name = card.name,
                    mainContact = card.mainContact,
                    imageUri = card.image.uri.toString(),
                    onClick = { onClickItemCard(card.id!!) }
                )
                HorizontalDivider(Modifier.padding(horizontal = 8.dp))
            }
        }
    }
}


@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    name: String,
    mainContact: String,
    imageUri: String,
) {
    Surface (
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box( contentAlignment = Alignment.Center ) {
                // Only used to signalize to the user there's a image there
                // TODO: Use onState from AsyncImage
                Icon(imageVector = Icons.Outlined.CreditCard, contentDescription = null)
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape) // TODO: Change to param
                        .size(40.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = mainContact,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardItemPreview() {
    BusyCardAppTheme {
        CardItem(
            name = "John Doe",
            mainContact = "johndoe@gmail.com",
            imageUri = "",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardsListingPreview() {
    BusyCardAppTheme {
        CardsListing(
            cards = listOf(
                Card(
                    id = "1",
                    name = "John Doe",
                    mainContact = "johndoe@gmail.com",
                ),
                Card(
                    id = "2",
                    name = "John Doe",
                    mainContact = "johndoe@gmail.com",
                )
            ),
            onClickItemCard = {},
            searchQuery = "",
            onSearchQueryChange = {},
        )
    }
}