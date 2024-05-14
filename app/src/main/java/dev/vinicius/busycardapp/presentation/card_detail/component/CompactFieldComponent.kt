package dev.vinicius.busycardapp.presentation.card_detail.component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme


@Composable
fun CompactFieldComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    @StringRes title: Int,
    text: String,
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
    ){
        Icon(
            modifier = modifier.size(24.dp),
            imageVector = imageVector,
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column {
           Text(
               text = stringResource(title),
               style = MaterialTheme.typography.titleMedium,
           )
           Text(
               text = text,
               style = MaterialTheme.typography.bodyMedium,
           )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CompactFieldComponentPreview() {
    BusyCardAppTheme {
        CompactFieldComponent(
            onClick = {},
            imageVector = Icons.Outlined.Phone,
            title = R.string.txt_texttype_phone,
            text = "123 Main Street, Anytown, USA",
        )
    }
}