package com.example.droidsoftthird.composable.map.place

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.droidsoftthird.PlaceMapViewState
import com.example.droidsoftthird.R
import com.example.droidsoftthird.model.domain_model.ViewPort
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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

        viewState.currentPoint?.let {
            Marker(
                icon = createCurrentLocationDot(LocalContext.current),
                state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                title = "現在地",
                onClick = { marker ->
                    fetchPlaceDetail((marker.tag.toString()))
                    true
                }
            )
        }

        if (viewState.placeDetail != null) { //TODO GeoCodeと統合させる。
            Marker(
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                state = MarkerState(position = LatLng(viewState.placeDetail.location.lat, viewState.placeDetail.location.lng)),
                title = viewState.placeDetail.name,
                onClick = { marker ->
                    fetchPlaceDetail((marker.tag.toString()))
                    true
                }
            )
        }

        if (viewState.reverseGeocode != null) {
            Marker(
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                state = MarkerState(position = LatLng(viewState.reverseGeocode.latitude, viewState.reverseGeocode.longitude)),
            )
        }

        if (viewState.places?.isNotEmpty() == true) {
            viewState.places.forEach {
                if(it.id == viewState.placeDetail?.id) return@forEach
                Marker(
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_ORANGE
                    ),
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

fun createCurrentLocationDot(context: Context): BitmapDescriptor {
    val padding = 8
    val strokeWidth = 2

    val circleDrawable = ShapeDrawable(OvalShape()).apply {
        paint.color = ContextCompat.getColor(context, R.color.primary_dark)
        intrinsicWidth = 36
        intrinsicHeight = 36
    }

    val paddingColor = Color.White
    val drawableWithPadding = ShapeDrawable(OvalShape()).apply {
        paint.color = paddingColor.toArgb()
        intrinsicWidth = circleDrawable.intrinsicWidth + padding
        intrinsicHeight = circleDrawable.intrinsicHeight + padding
    }

    val strokeDrawable = GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        setColor(Color.Transparent.toArgb())
        setSize(drawableWithPadding.intrinsicWidth + strokeWidth * 2, drawableWithPadding.intrinsicHeight + strokeWidth * 2)
        setStroke(strokeWidth, Color.Black.toArgb())
    }

    val layers = arrayOf(strokeDrawable, drawableWithPadding, circleDrawable)
    val layerDrawable = LayerDrawable(layers).apply {
        setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth)
        setLayerInset(2, padding + strokeWidth, padding + strokeWidth, padding + strokeWidth, padding + strokeWidth)
    }

    val bitmap = Bitmap.createBitmap(layerDrawable.intrinsicWidth, layerDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    layerDrawable.setBounds(0, 0, layerDrawable.intrinsicWidth, layerDrawable.intrinsicHeight)
    layerDrawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
