package dev.vinicius.busycardapp.presentation.card_detail.component

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.domain.model.card.Field

@Composable
fun CardInfoAddressField(
    modifier: Modifier = Modifier,
    field: Field.AddressField,
) {
    val TAG = "CardInfoAddressField"
    Log.d(TAG, "field: ${field.localization}")
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    val onCallIntent: (String) -> Unit = { uri ->
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(intent)
    }

    var showDialog by remember { mutableStateOf(false) }
    var copied by remember { mutableStateOf(false) }

    if (showDialog) {
        DialogComponent(
            onDismiss = { showDialog = false },
            onConfirm = { showDialog = false },
            confirmText = R.string.txt_close,
        ) {
            Column {
                TextButton(
                    onClick = {
                        onCallIntent("geo:0,0?q=${Uri.encode(field.textLocalization)}")
                    }
                ) {
                    Text(stringResource(R.string.txt_search_text_map))
                }
                Spacer(modifier = Modifier.height(2.dp))
                TextButton(
                    onClick = {
                        field.localization?.let {
                            onCallIntent("geo:${it.latitude},${it.longitude}")
                        }
                    },
                    enabled = field.localization != null
                ) {
                    Text(
                        if (field.localization != null)
                            stringResource(R.string.txt_search_location_map)
                        else
                            stringResource(R.string.txt_no_location_available)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                TextButton(
                    onClick = {
                        clipboard.setText(AnnotatedString(field.textLocalization))
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

    Row (
        modifier = Modifier.clickable {
            showDialog = true
        }
    ) {
        Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null)
        Text(text = field.textLocalization)
    }
}

@Composable
fun CompactAddressFieldComponent(
    modifier: Modifier = Modifier,
    textLocalization: String,
    localization: LatLng?
) {

    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    val onCallIntent: (String) -> Unit = { uri ->
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(intent)
    }

    var showDialog by remember { mutableStateOf(false) }
    var copied by remember { mutableStateOf(false) }

    if (showDialog) {
        DialogComponent(
            onDismiss = { showDialog = false },
            onConfirm = { showDialog = false },
            confirmText = R.string.txt_close,
        ) {
            Column {
                TextButton(
                    onClick = {
                        onCallIntent("geo:0,0?q=${Uri.encode(textLocalization)}")
                    }
                ) {
                    Text(stringResource(R.string.txt_search_text_map))
                }
                Spacer(modifier = Modifier.height(2.dp))
                TextButton(
                    onClick = {
                        localization?.let {
                            onCallIntent("geo:${it.latitude},${it.longitude}")
                        }
                    },
                    enabled = localization != null
                ) {
                    Text(
                        if (localization != null)
                            stringResource(R.string.txt_search_location_map)
                        else
                            stringResource(R.string.txt_no_location_available)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                TextButton(
                    onClick = {
                        clipboard.setText(AnnotatedString(textLocalization))
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

    CompactFieldComponent(
        onClick = {
            showDialog = true
        },
        imageVector = Icons.Outlined.LocationOn,
        title = R.string.txt_location,
        text = textLocalization
    )
}