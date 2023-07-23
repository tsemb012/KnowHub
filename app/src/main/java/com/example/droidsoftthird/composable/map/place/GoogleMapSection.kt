package com.example.droidsoftthird.composable.map.place

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

@Composable
fun GoogleMapScreen(
    viewState: State<PlaceMapViewState>,
    updateViewState: KFunction1<PlaceMapViewState, Unit>,
    fetchPlaceDetail: (placeId: String) -> Unit,
    notifyMapLoaded: () -> Unit,
) {
    val tokyo = LatLng(35.681236, 139.767125)
    val defaultCameraPosition = CameraPosition.fromLatLngZoom(tokyo, 11f)//これを現在地に変更する。
    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    } //TODO 上のレオや０にあげれるか確認する。
    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier,
        properties = MapProperties(mapType = MapType.NORMAL),
        uiSettings = MapUiSettings(compassEnabled = false),
        onMapLoaded = { notifyMapLoaded()},
        onPOIClick = { },
        content = GoogleMapContent(
            cameraPositionState,
            viewState.value,
            updateViewState,
            fetchPlaceDetail
        ),
    )
}
