package dev.vinicius.busycardapp.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.vinicius.busycardapp.domain.model.card.Card

@Composable
fun CardsListing(
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
            CardItem(
                name = card.name,
                mainContact = card.mainContact,
                imageUri = card.image.uri.toString(),
                onClick = { onClickItemCard(card.id!!) }
            )
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
            .fillMaxWidth()
            .height(45.dp),
        onClick = onClick
    ){
        Row {
            Box( contentAlignment = Alignment.Center ) {
                // Only used to signalize to the user there's a image there
                // TODO: Use onState from AsyncImage
                CircularProgressIndicator()
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape) // TODO: Change to param
                        .size(40.dp)
                )
            }
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
