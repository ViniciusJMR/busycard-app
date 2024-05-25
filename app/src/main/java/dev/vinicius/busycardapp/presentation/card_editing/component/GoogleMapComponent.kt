package dev.vinicius.busycardapp.presentation.card_editing.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.core.getCurrentLocation


@Composable
fun GoogleMapComponent(
    modifier: Modifier = Modifier,
    onMapClick: (LatLng) -> Unit,
    onClearMarkers: () -> Unit,
    onConfirm: () -> Unit,
    latLng: LatLng?,
) {
    val context = LocalContext.current
    val TAG = "GoogleMapComponent"

    var location by remember { mutableStateOf(latLng) }

    // TODO: Remove hardcoded location
    // Currently, it's in Brasília, Brazil
    val brasiliaLocation = LatLng(-15.793889, -47.882778)

    var shouldFetchLocation by remember { mutableStateOf(true) }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true,
            )
        )
    }

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                mapToolbarEnabled = false,
                tiltGesturesEnabled = false,
            )
        )
    }


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location ?: brasiliaLocation, 15f)
        Log.d(TAG, "GoogleMapComponent: setando cameraPositionState")
    }

    LaunchedEffect(key1 = true) {
        if (location == null) {
            getCurrentLocation(context,
                onLocationFetched = {
                    location = it
                    onMapClick(it)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
                    Log.d(TAG, "GoogleMapComponent: Fetchou")
                    shouldFetchLocation = false
                },
                onLocationError = {
                    Log.d(TAG, "GoogleMapComponent: Error getting location")
                }
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings,
            onMapClick = {
                location = it
                onMapClick(it)
            },
        ) {
            location?.let {
                Marker(
                    state = MarkerState(position = it),
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row (horizontalArrangement = Arrangement.End) {
                Button(onClick = onConfirm) { Text(stringResource(R.string.txt_ok)) }
            }
            Button(onClick = {
                location = null
                onClearMarkers()
            }) { Text(stringResource(R.string.txt_clear_position)) }
        }

    }
}