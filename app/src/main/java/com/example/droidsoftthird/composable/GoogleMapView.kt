package com.example.droidsoftthird.composable.map

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.droidsoftthird.model.domain_model.Place
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    updateCameraPosition: (northEast: LatLng, southWest: LatLng) -> Unit = { _, _ -> },
    places: MutableState<List<Place>>,
    currentPoint: MutableState<LatLng>,
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
        //TODO typeを選択で切るように追加でComposeを設置するように
        //TODO でふぉるとではnullでoptionで外側から追加UIをセットするようにする。
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
                    currentPoint.value = it.center
                }
            }
            places.value.forEach {
                Marker(
                        state = MarkerState(position = LatLng(it.location.lat, it.location.lng)),
                        tag = it.placeId,
                        title = it.name,
                        snippet = it.types[0],
                        //onClick = { onMarkerClick(it.tag) }
                        //TODO ViewModelからidを使って、Placeの詳細を取得するようにする。
                        //TODO モーダルを出現させてから詳細を取得するようにするのが良いんじゃないか？
                )
            }
            //TODO Circleは現状そこまで必要じゃないからあと回しにする、。
        }
    }
}

