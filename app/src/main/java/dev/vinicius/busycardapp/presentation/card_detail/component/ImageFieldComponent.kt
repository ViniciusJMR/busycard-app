package dev.vinicius.busycardapp.presentation.card_detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.vinicius.busycardapp.domain.model.card.Field

@Composable
fun CardInfoImageField(
    modifier: Modifier = Modifier,
    field: Field.ImageField,
) {
    Box( contentAlignment = Alignment.Center ) {
        // Only used to signalize to the user there's a image there
        // TODO: Use onState from AsyncImage
        CircularProgressIndicator()
        AsyncImage(
            model = field.image.path,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(CircleShape) // TODO: Change to param
                .size(field.size.dp)
        )
    }
}
