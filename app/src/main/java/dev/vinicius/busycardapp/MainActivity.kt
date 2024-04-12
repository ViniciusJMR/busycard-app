package dev.vinicius.busycardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.busycardapp.presentation.card_creation.CardCreationViewModel
import dev.vinicius.busycardapp.presentation.card_creation.CardCreationScreen
import dev.vinicius.busycardapp.presentation.card_info.CardInfoScreen
import dev.vinicius.busycardapp.presentation.shared_cards.SharedCardsScreen
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusyCardAppTheme {
                val viewModel = hiltViewModel<CardCreationViewModel>()
                val state by viewModel.state.collectAsState()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    CardInfoScreen()
//                    CardCreationScreen()
                    SharedCardsScreen()
                }
            }
        }
    }
}
