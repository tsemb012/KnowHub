package com.example.droidsoftthird.composable.map

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.composable.BottomModal
import com.example.droidsoftthird.model.domain_model.Place
import com.example.droidsoftthird.model.domain_model.PlaceDetail
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    updateCameraPosition: (northEast: LatLng, southWest: LatLng) -> Unit = { _, _ -> },
    placesLoadState: MutableState<LoadState>,
    placeDetailLoadState: MutableState<LoadState>,
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
    val isLoading: Boolean = placesLoadState.value is LoadState.Loading
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

        BottomModal(bottomSheetState) {
            Column {
                if (isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
                Box {
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
                                    currentRadius.value = distanceInMeters(it.center.latitude,
                                        it.center.longitude,
                                        it.center.latitude,
                                        it.northeast.longitude).toInt()
                                }
                            } else {
                                placeDetailLoadState.value = LoadState.Initialized
                            }

                            when (placesLoadState.value) {
                                is LoadState.Error -> {
                                    Log.d("tsemb012", placesLoadState.value.getErrorOrNull().toString())
                                    //Toast.makeText(LocalContext.current, placesLoadState.value.getErrorOrNull().toString(), Toast.LENGTH_SHORT).show()
                                }
                                is LoadState.Loaded<*> -> {
                                    placesLoadState.value.getValueOrNull<List<Place>>()?.forEach {
                                        Marker(
                                            state = MarkerState(position = LatLng(it.location.lat, it.location.lng)),
                                            tag = it.placeId,
                                            title = it.name,
                                            snippet = it.types[0],
                                            onClick = { marker ->
                                                onMarkerClick(marker.tag.toString())
                                                true
                                            }
                                        )
                                    }
                                }
                            }

                            when (placeDetailLoadState.value) {
                                is LoadState.Error -> {
                                    Toast.makeText(LocalContext.current, placeDetailLoadState.value.getErrorOrNull().toString(), Toast.LENGTH_SHORT).show()
                                }
                                is LoadState.Loaded<*> -> {
                                    placeDetailLoadState.value.getValueOrNull<PlaceDetail>()?.let {
                                        scope.launch {
                                            bottomSheetState.show()
                                        }
                                    }
                                }
                            }
                        }
                        Column {
                            Row(modifier = Modifier
                                .height(56.dp)
                                .padding(top = 16.dp)) {
                                composableSearchBox()
                                composableDropDown()
                            }
                            composableChipGroup()
                        }
                    }
                }
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
