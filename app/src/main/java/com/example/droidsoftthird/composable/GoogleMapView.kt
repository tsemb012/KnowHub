package com.example.droidsoftthird.composable.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        currentRadius: MutableState<Int>,
        onMarkerClick: (String) -> Unit = {},
        composableSearchBox: @Composable () -> Unit = {},
        composableDropDown: @Composable () -> Unit = {},
        composableChipGroup: @Composable () -> Unit = {},
) {
    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    var shouldAnimateZoom by remember { mutableStateOf(true) }
    var ticker by remember { mutableStateOf(0) }
    var mapProperties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }
    var mapVisible by remember { mutableStateOf(true) }


    if (mapVisible) {
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
                    currentRadius.value = distanceInMeters(it.center.latitude, it.center.longitude, it.center.latitude, it.northeast.longitude).toInt()
                }
            }
            places.value.forEach {
                Marker(
                        state = MarkerState(position = LatLng(it.location.lat, it.location.lng)),
                        tag = it.placeId,
                        title = it.name,
                        snippet = it.types[0],
                        onClick = { marker ->
                            onMarkerClick(marker.tag.toString())
                            true //TODO このBooleanの意味を後で確認する。
                        }
                        //TODO ViewModelからidを使って、Placeの詳細を取得するようにする。
                        //TODO モーダルを出現させてから詳細を取得するようにするのが良いんじゃないか？
                )
            }
            //TODO Circleは現状そこまで必要じゃないからあと回しにする、。
        }
        Column {
            Row(modifier = Modifier.height(56.dp).padding(top = 16.dp)) {
                composableSearchBox()
                composableDropDown()
            }
            composableChipGroup()
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
} //TODO 精度をあげる。
