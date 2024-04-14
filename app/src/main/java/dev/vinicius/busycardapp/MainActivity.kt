package dev.vinicius.busycardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import dagger.hilt.android.AndroidEntryPoint
import dev.vinicius.busycardapp.presentation.NavGraphs
import dev.vinicius.busycardapp.presentation.appCurrentDestinationAsState
import dev.vinicius.busycardapp.presentation.destinations.MyCardsScreenDestination
import dev.vinicius.busycardapp.presentation.destinations.SharedCardsScreenDestination
import dev.vinicius.busycardapp.presentation.startAppDestination
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusyCardAppTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                val appState = rememberAppState(navController)
                Scaffold(
                    bottomBar = {
                        if (appState.shouldShowBottomBar)
                            HomeBottomNavBar(navController = navController)
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun rememberAppState(
        navController: NavController
    ) =
        remember(navController) {
            AppState(navController)
        }

    data class AppState(
        val navController: NavController,
    ) {

        val bottomBarTabs = BottomBarDestination.entries
        private val bottomBarRoutes = bottomBarTabs.map { it.direction.route }

        val shouldShowBottomBar: Boolean
            @Composable get () = navController
                .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

    }

    enum class BottomBarDestination(
        val direction: DirectionDestinationSpec,
        val icon: ImageVector,
        @StringRes val label: Int
    ) {
        SharedCards(SharedCardsScreenDestination, Icons.Filled.CardMembership, R.string.label_bottomnav_shared_screen),
        MyCards(MyCardsScreenDestination, Icons.Outlined.CreditCard, R.string.label_bottomnav_mycards_screen)
    }

    @Composable
    private fun HomeBottomNavBar(
        navController: NavController
    ) {
        val currentDestination = navController.appCurrentDestinationAsState().value
            ?: NavGraphs.root.startAppDestination
        NavigationBar {
            BottomBarDestination.entries.forEach { destination ->
                NavigationBarItem(
                    selected = currentDestination == destination.direction,
                    onClick = {
                        navController.navigate(destination.direction) {
                            popUpTo(NavGraphs.root) {
                                saveState = true
                            }

                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(destination.icon, contentDescription = stringResource(destination.label)) },
                    label = { Text(stringResource(destination.label)) }
                )
            }
        }
    }
}
