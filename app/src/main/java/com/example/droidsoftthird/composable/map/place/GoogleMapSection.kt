package com.example.droidsoftthird.composable.map.place

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.example.droidsoftthird.PlaceMapViewState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GoogleMapScreen(
    viewState: State<PlaceMapViewState>,
    updateViewState: KFunction1<PlaceMapViewState, Unit>,
    fetchPlaceDetail: (placeId: String) -> Unit,
    bottomSheetState: ModalBottomSheetState,
    reverseGeocode: (LatLng) -> Unit,
    notifyMapLoaded: () -> Unit,
) {
    val tokyo = LatLng(35.681236, 139.767125)
    val currentLocation = viewState.value.currentPoint ?: tokyo
    val defaultCameraPosition = CameraPosition.fromLatLngZoom(currentLocation, 15f)//これを現在地に変更する。
    val cameraPositionState = rememberCameraPositionState { position = defaultCameraPosition }

    if (viewState.value.singlePlace != null && bottomSheetState.currentValue != ModalBottomSheetValue.Hidden) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            viewState.value.singlePlace?.let { LatLng(it.lat - 0.004, it.lng) } ?: tokyo,
            15f
        )
    }

 /*   if (viewState.value.singlePlace != null && bottomSheetState.currentValue == ModalBottomSheetValue.Hidden) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            viewState.value.singlePlace?.let { LatLng(it.lat - 0.004, it.lng) } ?: tokyo,
            15f
        )
    }*/

    GoogleMap(
        cameraPositionState = cameraPositionState ,
        modifier = Modifier,
        properties = MapProperties(mapType = MapType.NORMAL),
        uiSettings = MapUiSettings(compassEnabled = false),
        onMapLoaded = { notifyMapLoaded()},
        onMapLongClick = { latLng -> reverseGeocode(latLng) },
        content = GoogleMapContent(
            cameraPositionState,
            viewState.value,
            updateViewState,
            fetchPlaceDetail
        ),
    )
}
