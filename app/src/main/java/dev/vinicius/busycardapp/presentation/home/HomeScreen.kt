package dev.vinicius.busycardapp.presentation.home

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.ContextCompat.getString
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.presentation.destinations.SharedCardsScreenDestination

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Shared( SharedCardsScreenDestination, Icons.Filled.CardMembership, R.string.label_bottomnav_shared_screen)

}



@Composable
fun HomeBottomNavBar(
    modifier: Modifier = Modifier,

    ) {

}


@Destination(start = true)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,

) {

}