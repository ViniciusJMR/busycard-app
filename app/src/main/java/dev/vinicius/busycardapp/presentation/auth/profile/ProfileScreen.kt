package dev.vinicius.busycardapp.presentation.auth.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.vinicius.busycardapp.presentation.NavGraphs
import dev.vinicius.busycardapp.presentation.destinations.LoginScreenDestination
import dev.vinicius.busycardapp.presentation.startAppDestination


@Destination
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState()

    Box {
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ){
                Text(text = state.value.name)
                Text(text = state.value.surname)
            }
            Text(text = state.value.username)
            Text(text = state.value.email)

        }

        TextButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = {
                viewModel.logout()
                navigator.popBackStack()
                navigator.navigate(LoginScreenDestination)
            }
        ) {
            Text(text = "Sair da conta")
        }

    }
}