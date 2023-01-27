package com.example.droidsoftthird.composable.map

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.droidsoftthird.composable.SearchBox
import com.example.droidsoftthird.model.domain_model.Place
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun GoogleMapView(
        modifier: Modifier = Modifier,
        cameraPositionState: CameraPositionState = rememberCameraPositionState(),
        onMapLoaded: () -> Unit = {},
        updateCameraPosition: (northEast: LatLng, southWest: LatLng) -> Unit = { _, _ -> },
        places: List<Place>,
        onMarkerClick: (String) -> Unit = {},
        searchBox: @Composable () -> Unit = {},
) {
    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    var shouldAnimateZoom by remember { mutableStateOf(true) }
    var ticker by remember { mutableStateOf(0) }
    var mapProperties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }
    var mapVisible by remember { mutableStateOf(true) }


    if (mapVisible) {

        searchBox()
        GoogleMap(
                modifier = modifier,
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = uiSettings,
                onMapLoaded = onMapLoaded,
                onPOIClick = { },
        ) {
            if (!cameraPositionState.isMoving) {//カメラの動きが止まった時のデータをViewModelにあげるようにする。
                cameraPositionState.projection?.visibleRegion?.latLngBounds?.let {
                    updateCameraPosition(it.northeast, it.southwest)
                }
            }
            /*places.forEach {
                Marker(
                        state = MarkerState(position = LatLng(it.location.latitude, it.location.longitude)),
                        tag = it.placeId,
                        title = it.name,
                        snippet = it.type,
                        onClick = { onMarkerClick(it.tag) }
                        //TODO ViewModelからidを使って、Placeの詳細を取得するようにする。
                        //TODO モーダルを出現させてから詳細を取得するようにするのが良いんじゃないか？
                        //TODO

                )
            }*/
        }
        searchBox()
    }
}

