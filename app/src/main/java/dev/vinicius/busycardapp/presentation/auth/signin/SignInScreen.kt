package dev.vinicius.busycardapp.presentation.auth.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.core.presentation.component.TextFieldComponent
import dev.vinicius.busycardapp.presentation.auth.login.LoginEffect
import dev.vinicius.busycardapp.presentation.auth.login.LoginEvent
import dev.vinicius.busycardapp.presentation.auth.login.LoginViewModel
import dev.vinicius.busycardapp.presentation.destinations.SharedCardsScreenDestination

@Destination
@Composable
fun SignInScreen(
    navigator: DestinationsNavigator,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState()

    val event = viewModel::onEvent

    LaunchedEffect(effect) {
        effect?.let {
            when(it) {
                SignInEffect.NavigateToHome -> {
                    navigator.navigate(SharedCardsScreenDestination)
                }
            }
            viewModel.resetEffect()
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextFieldComponent(
            value = state.name,
            onValueChange = { event(SignInEvent.OnNameChange(it)) },
            label = { Text(stringResource(R.string.label_name))},
            singleLine = true,
        )

        TextFieldComponent(
            singleLine = true,
            value = state.surname,
            onValueChange = { event(SignInEvent.OnSurnameChange(it)) },
            label = { Text(stringResource(R.string.label_surname))}
        )

        TextFieldComponent(
            singleLine = true,
            value = state.username,
            onValueChange = { event(SignInEvent.OnUsernameChange(it)) },
            label = { Text(stringResource(R.string.label_username))}
        )

        TextFieldComponent(
            singleLine = true,
            value = state.email,
            onValueChange = { event(SignInEvent.OnEmailChange(it)) },
            label = { Text(stringResource(R.string.label_email))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        TextFieldComponent(
            singleLine = true,
            value = state.password,
            onValueChange = { event(SignInEvent.OnPasswordChange(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(stringResource(R.string.label_password))}
        )

        TextFieldComponent(
            singleLine = true,
            value = state.password2,
            onValueChange = { event(SignInEvent.OnPassword2Change(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(stringResource(R.string.label_password2))}
        )

        OutlinedButton(onClick = { event(SignInEvent.OnSignIn) }) {
            Text("Criar")
        }
    }
}
