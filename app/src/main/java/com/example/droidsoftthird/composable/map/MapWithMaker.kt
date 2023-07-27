package com.example.droidsoftthird.composable.map

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.droidsoftthird.model.domain_model.EventDetail
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapWithMarker(event: MutableState<EventDetail?>, modifier: Modifier = Modifier) {
    val place = event.value?.place

    if (place != null) {
        val eventPosition = CameraPosition.fromLatLngZoom(LatLng(place.location.lat, place.location.lng), 15f)
        val cameraPositionState = rememberCameraPositionState{ position = eventPosition }
        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = modifier.clip(RoundedCornerShape(10)),
            properties = MapProperties(mapType = MapType.NORMAL),
            uiSettings = MapUiSettings(
                compassEnabled = false,
                scrollGesturesEnabled = false,
                zoomGesturesEnabled = false,
            ),
        ) {
            Marker(
                state = MarkerState(position = LatLng(place.location.lat, place.location.lng)),
                tag = place.placeId,
                title = place.name,
                snippet = place.category,
            )
        }
    }
}
