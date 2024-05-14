package dev.vinicius.busycardapp.presentation.card_detail.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.TextType

@Composable
fun CardInfoTextField(
    modifier: Modifier = Modifier,
    field: Field.TextField,
) {
    val TAG = "CardInfoTextField"
    var text by remember { mutableIntStateOf(R.string.txt_dial_number) }
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    val onCallIntent : Intent? by remember { mutableStateOf(
        when (field.textType) {
            TextType.PHONE -> {
                text = R.string.txt_dial_number
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:${field.value}"))
            }
            TextType.EMAIL -> {
                text = R.string.txt_send_email_to
                Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${field.value}"))
            }
            else -> { null }
        }
    )}

    var showDialog by remember { mutableStateOf(false) }
    var copied by remember { mutableStateOf(false) }

    if (showDialog) {
        DialogComponent(
            onDismiss = { showDialog = false },
            onConfirm = { showDialog = false },
            confirmText = R.string.txt_close,
        ) {
            Column {
                onCallIntent?.let {
                    TextButton(
                        onClick = { context.startActivity(it) }
                    ) {
                        Text(text = stringResource(text))
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                }
                TextButton(
                    onClick = {
                        clipboard.setText(AnnotatedString(field.value))
                        copied = true
                    }
                ) {
                    Text(
                        stringResource(
                            if (!copied) R.string.txt_copy else R.string.txt_copied
                        )
                    )
                }
            }
        }
    }
    Text(
        modifier = Modifier.clickable {
            showDialog = true
        },
        text = field.value
    )
}
