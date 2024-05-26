package dev.vinicius.busycardapp.core.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.presentation.auth.signin.SignInEvent


@Composable
fun TextFieldComponent(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    singleLine: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = label,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
    )
}
