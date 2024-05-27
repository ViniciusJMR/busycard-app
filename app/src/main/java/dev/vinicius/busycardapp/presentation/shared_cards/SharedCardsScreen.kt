package dev.vinicius.busycardapp.presentation.shared_cards

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.core.presentation.CardsListing
import dev.vinicius.busycardapp.presentation.destinations.CardDetailScreenDestination


// Currently being used to check every card
@RootNavGraph(start = true)
@Destination
@Composable
fun SharedCardsScreen(
    navigator: DestinationsNavigator,
    viewModel: SharedCardsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    val barcodeLauncher: ActivityResultLauncher<ScanOptions> = rememberLauncherForActivityResult(
        ScanContract()
    ) { result ->
        result.contents?.let {
            navigator.navigate(CardDetailScreenDestination(id = it, saveAsSharedCard = true))
        }
    }

    // TODO: Change to Scarfold
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    barcodeLauncher.launch(
                        ScanOptions().apply {
                            setBeepEnabled(false)
                            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                            setOrientationLocked(false)
                        }
                    )
                }
            ) {
                Icon(Icons.Filled.QrCodeScanner, contentDescription = "")
            }
        }
    ){
        if (!state.isLoading) {
            CardsListing(
                modifier = Modifier.padding(it),
                onClickItemCard = { id ->
                    navigator.navigate(CardDetailScreenDestination(id = id))
                },
                cards = state.cards,
                searchQuery = state.searchQuery,
                onSearchQueryChange = { query ->
                    viewModel.onEvent(SharedCardsEvent.OnSearchQueryChange(query))
                },
                isRefreshing = state.isRefreshing,
                onRefresh = { viewModel.onEvent(SharedCardsEvent.Refresh) }
            )
        } else {
            Text(stringResource(R.string.txt_loading))
        }
    }
}
