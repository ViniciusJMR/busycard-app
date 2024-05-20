package dev.vinicius.busycardapp.core.presentation.component

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.vinicius.busycardapp.R

@Composable
fun DialogComponent(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    @StringRes confirmText: Int = R.string.txt_ok,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        text = content,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(stringResource(confirmText))
            }
        },
    )
}
