package com.example.droidsoftthird.composable.map.place

import androidx.compose.runtime.Composable
import com.example.droidsoftthird.PlaceMapViewState
import com.example.droidsoftthird.model.domain_model.ViewPort
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun GoogleMapContent(
    cameraPositionState: CameraPositionState,
    viewState: PlaceMapViewState,
    updateViewState: (PlaceMapViewState) -> Unit,
    fetchPlaceDetail: (placeId: String) -> Unit,

): @Composable () -> Unit =
    {
        val updateCameraPosition: (northEast: LatLng, southWest: LatLng) -> Unit = { _, _ -> }
        if (!cameraPositionState.isMoving) {//カメラの動きが止まった時のデータをViewModelにあげるようにする。
            cameraPositionState.projection?.visibleRegion?.latLngBounds?.let {
                updateCameraPosition(it.northeast, it.southwest)
                cameraPositionState
                updateViewState(viewState.copy(
                    centerPoint = it.center,
                    radius = distanceInMeters(
                        it.center.latitude,
                        it.center.longitude,
                        it.center.latitude,
                        it.northeast.longitude
                    ).toInt(),
                    viewPort = ViewPort(
                        northEast = it.northeast,
                        southWest = it.southwest
                    ),
                ))
            }
        }

        if (viewState.places?.isNotEmpty() == true) {
            viewState.places.forEach {
                Marker(
                    state = MarkerState(position = LatLng(it.location.lat, it.location.lng)),
                    tag = it.id,
                    title = it.name,
                    onClick = { marker ->
                        fetchPlaceDetail((marker.tag.toString()))
                        true
                    }
                )
            }
        }
    }

private fun distanceInMeters(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
    val earthRadius = 6371.0 // 地球の半径
    val dLat = Math.toRadians(lat2 - lat1)
    val dLng = Math.toRadians(lng2 - lng1)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
        Math.sin(dLng / 2) * Math.sin(dLng / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    return (earthRadius * c * 1000.0) * 0.8
}