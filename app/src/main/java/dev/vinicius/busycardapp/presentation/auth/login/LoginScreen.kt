package dev.vinicius.busycardapp.presentation.auth.login

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.core.presentation.component.TextFieldComponent
import dev.vinicius.busycardapp.presentation.destinations.SharedCardsScreenDestination
import dev.vinicius.busycardapp.presentation.destinations.SignInScreenDestination

@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState()

    val event = viewModel::onEvent


    LaunchedEffect(effect) {
        effect?.let {
            when(it) {
                LoginEffect.NavigateToHome -> {
                    navigator.navigate(SharedCardsScreenDestination)
                }

                LoginEffect.NavigateToCreateAccount -> {
                    navigator.navigate(SignInScreenDestination)
                }
            }
            viewModel.resetEffect()
        }
    }


    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextFieldComponent(
            value = state.email,
            onValueChange = { event(LoginEvent.OnEmailChange(it))},
            label = { Text(stringResource(R.string.label_email))},
            singleLine = true,
        )

        TextFieldComponent(
            singleLine = true,
            value = state.password,
            onValueChange = { event(LoginEvent.OnPasswordChange(it))},
            label = { Text(stringResource(R.string.label_password))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        Spacer(modifier = Modifier.padding(8.dp))
        TextButton(onClick = { event(LoginEvent.OnSignInClicked)  }) {
            Text("Criar Conta")
        }
        Spacer(modifier = Modifier.padding(8.dp))


        OutlinedButton(onClick = { event(LoginEvent.OnLogin) }) {
            Text(stringResource(R.string.txt_login))
        }
    }
}