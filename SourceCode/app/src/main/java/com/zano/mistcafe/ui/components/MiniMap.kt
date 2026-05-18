package com.zano.mistcafe.ui.components
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MiniMapCard(
    latitude: Double,
    longitude: Double
) {
    val location = LatLng(latitude, longitude)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    LaunchedEffect(location) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(location, 15f),
            durationMs = 1000
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp), // Slightly taller
        shape = RoundedCornerShape(24.dp), // More rounded
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    scrollGesturesEnabled = false,
                    zoomGesturesEnabled = false,
                    tiltGesturesEnabled = false,
                    compassEnabled = false,
                    myLocationButtonEnabled = false
                )
            ) {
                Marker(
                    state = MarkerState(position = location),
                    title = "Shop Location"
                )
            }

            // Optional: Overlay to indicate it's not fully interactive
            // or just a border
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.Black.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            )
        }
    }
}