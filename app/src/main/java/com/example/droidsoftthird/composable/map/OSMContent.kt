package com.example.droidsoftthird.composable.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.location.LocationManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.droidsoftthird.*
import com.example.droidsoftthird.model.domain_model.AreaCategory
import com.example.droidsoftthird.model.domain_model.GroupCountByArea
import com.example.droidsoftthird.model.presentation_model.GroupLocationsUiModel
import com.example.droidsoftthird.model.presentation_model.groupCountByArea
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OSMContent(
    uiModel: State<GroupLocationsUiModel?>,
    fragment: GroupLocationsFragment,
    bottomSheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    getGroups: (Int, AreaCategory) -> Unit
) {
    val groupCountByCity = uiModel.groupCountByArea?.filter { it.category == AreaCategory.CITY }
    val groupCountByPrefecture = mutableListOf(uiModel.groupCountByArea?.filter { it.category ==  AreaCategory.PREFECTURE })
    val isLoading = uiModel.value?.isLoading ?: false

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            update = updateMapView(groupCountByCity, getGroups, scope, bottomSheetState),
            factory = mapViewFactory(fragment),
            modifier = Modifier.fillMaxSize()
        )
        if (isLoading) {
            LinearProgressIndicator(
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(), color = colorResource(id = R.color.primary_dark), trackColor = colorResource(
                    id = R.color.base_100
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun updateMapView(
    groupCountByCity: List<GroupCountByArea>?,
    getGroups: (Int, AreaCategory) -> Unit,
    scope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
) = { mapView: MapView ->
    val cityMarkers = groupCountByCity?.mapNotNull { groupCount ->
        val city = groupCount.cityName ?: return@mapNotNull null
        val location = GeoPoint(groupCount.latitude, groupCount.longitude)
        Marker(mapView).apply {
            position = location
            title = city
            snippet = "${groupCount.groupCount} groups"
            icon = createCustomMarker(mapView.context, groupCount.groupCount)
            setOnMarkerClickListener { marker, mapView ->
                val city = marker.title ?: return@setOnMarkerClickListener false
                //selectedCityGroups.clear()
                val newCenter = GeoPoint(
                    marker.position.latitude - 0.015,
                    marker.position.longitude
                ) // 緯度を少し減らす
                mapView.controller.apply {
                    animateTo(newCenter)
                    setZoom(15.0)
                }
                showInfoWindow()
                getGroups(groupCount.code.toInt(), groupCount.category)
                scope.launch { bottomSheetState.show() }

                true
            }
        }
    }
    cityMarkers?.forEach { mapView.overlays.add(it) }
    mapView.invalidate()
}

@Composable
private fun mapViewFactory(fragment: GroupLocationsFragment) = { context: Context ->

    MapView(context).apply {

        setTileSource(TileSourceFactory.MAPNIK)
        zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        setMultiTouchControls(true)

        val locationManager = fragment.requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val defaultTokyoLocation = GeoPoint(35.681236, 139.767125)
            currentLocation.let {
                controller.apply {
                    setZoom(13)
                    setCenter(
                        GeoPoint(
                            it?.latitude ?: defaultTokyoLocation.latitude,
                            it?.longitude ?: defaultTokyoLocation.longitude
                        )
                    )
                }
                val currentLocationMarker = Marker(this).apply {
                    position = GeoPoint(
                        it?.latitude ?: defaultTokyoLocation.latitude,
                        it?.longitude ?: defaultTokyoLocation.longitude
                    )
                    icon = createCurrentLocationDotDrawable(fragment.requireContext())
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                }
                overlays.add(currentLocationMarker)
            }
        } else {
            controller.animateTo(GeoPoint(35.681236, 139.767125))
            controller.setZoom(7.0)
        }
    }
}

private fun createCustomMarker(context: Context, groupCount: Int): Drawable {
    val markerDrawable = ContextCompat.getDrawable(context, R.drawable.marker_default_focused_base)

    val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    val bitmap = Bitmap.createBitmap(markerDrawable?.intrinsicWidth!!, markerDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    markerDrawable.setBounds(0, 0, canvas.width, canvas.height)
    markerDrawable.draw(canvas)

    val xPos = canvas.width / 2
    val yPos = (canvas.height / 2 - (textPaint.descent() + textPaint.ascent()) / 2)
    canvas.drawText(groupCount.toString(), xPos.toFloat(), yPos, textPaint)

    return BitmapDrawable(context.resources, bitmap)
}

private fun createCurrentLocationDotDrawable(context: Context): Drawable {
    val padding = 8 // you can adjust this value as per your needs
    val strokeWidth = 2 // you can adjust this value as per your needs

    val circleDrawable = ShapeDrawable(OvalShape()).apply {
        paint.color = context.getColor(R.color.primary_dark)
        intrinsicWidth = 36
        intrinsicHeight = 36
    }

    val paddingColor = Color.WHITE
    val drawableWithPadding = ShapeDrawable(OvalShape()).apply {
        paint.color = paddingColor
        intrinsicWidth = circleDrawable.intrinsicWidth + padding
        intrinsicHeight = circleDrawable.intrinsicHeight + padding
    }

    val strokeDrawable = GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        setColor(Color.TRANSPARENT)
        setSize(drawableWithPadding.intrinsicWidth + strokeWidth * 2, drawableWithPadding.intrinsicHeight + strokeWidth * 2)
        setStroke(strokeWidth, Color.BLACK) // set stroke color here
    }

    val layers = arrayOf(strokeDrawable, drawableWithPadding, circleDrawable)
    val layerDrawable = LayerDrawable(layers).apply {
        setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth) // apply stroke to the drawable with padding
        setLayerInset(2, padding + strokeWidth, padding + strokeWidth, padding + strokeWidth, padding + strokeWidth) // apply padding to blue dot
    }

    layerDrawable.setBounds(0, 0, layerDrawable.intrinsicWidth, layerDrawable.intrinsicHeight)
    return layerDrawable
}

