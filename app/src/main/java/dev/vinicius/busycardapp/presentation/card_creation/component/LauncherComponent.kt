package dev.vinicius.busycardapp.presentation.card_creation.component

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LauncherForActivityResultComponent(
    onLauncherResult: (Uri) -> Unit,
    content: @Composable (ManagedActivityResultLauncher<String, Uri?>) -> Unit,
) {
    val launcher: ManagedActivityResultLauncher<String, Uri?> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onLauncherResult(uri)
        }
    }
    content(launcher)
}