package dev.vinicius.busycardapp.presentation.auth.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.vinicius.busycardapp.R
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


    Column {
        OutlinedTextField(
            value = state.name,
            onValueChange = { event(SignInEvent.OnNameChange(it)) },
            label = { Text(stringResource(R.string.label_name))}
        )

        OutlinedTextField(
            value = state.surname,
            onValueChange = { event(SignInEvent.OnSurnameChange(it)) },
            label = { Text(stringResource(R.string.label_surname))}
        )

        OutlinedTextField(
            value = state.username,
            onValueChange = { event(SignInEvent.OnUsernameChange(it)) },
            label = { Text(stringResource(R.string.label_username))}
        )

        OutlinedTextField(
            value = state.email,
            onValueChange = { event(SignInEvent.OnEmailChange(it)) },
            label = { Text(stringResource(R.string.label_email))}
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = { event(SignInEvent.OnPasswordChange(it)) },
            label = { Text(stringResource(R.string.label_password))}
        )

        OutlinedTextField(
            value = state.password2,
            onValueChange = { event(SignInEvent.OnPassword2Change(it)) },
            label = { Text(stringResource(R.string.label_password2))}
        )

        OutlinedButton(onClick = { event(SignInEvent.OnSignIn) }) {
            Text(stringResource(R.string.txt_login))
        }
    }
}
