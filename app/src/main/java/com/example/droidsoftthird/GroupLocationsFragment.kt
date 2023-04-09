package com.example.droidsoftthird

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.model.domain_model.GroupCountByArea
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupLocationsFragment:Fragment() {

    private val viewModel:GroupLocationsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getGroupCountByArea()
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osmdroid", 0))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SubComposeView(viewModel)
            }
        }
    }

}

@Composable
fun SubComposeView(viewModel: GroupLocationsViewModel) {
    viewModel.groupCountByArea.observeAsState().let { state ->
        if (state.value != null) {
            OSMMapView(state.value as List<GroupCountByArea>)
        } else {
            Text(text = "Loading...")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OSMMapView(groupCountByAreas: List<GroupCountByArea>) {
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val groupCountByCity = groupCountByAreas.filter { it.category == "city" }
    val groupCountByPrefecture = groupCountByAreas.filter { it.category == "prefecture" }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            Button(onClick = { Unit }) {
                Text(text = "Create a new group")
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = { context ->
                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setBuiltInZoomControls(true)
                    setMultiTouchControls(true)

                    // 緯度と経度を設定します。//TODO 自分の現在地をスタートとする。
                    val latitude = 35.6895 // 東京の緯度
                    val longitude = 139.6917 // 東京の経度
                    val zoomLevel = 12.0 // ズームレベル（1〜20）



                    controller.apply {
                        setZoom(zoomLevel)
                        setCenter(GeoPoint(latitude, longitude))
                    }

                    val cityMarkers = groupCountByCity.mapNotNull { groupCount ->
                        val city = groupCount.cityName ?: return@mapNotNull null
                        val location = GeoPoint(groupCount.latitude, groupCount.longitude)
                        Marker(this).apply {
                            position = location
                            title = city
                            snippet = "${groupCount.groupCount} groups"
                            icon = createCustomMarker(context, groupCount.groupCount)
                            setOnMarkerClickListener { marker, mapView ->
                                val city = marker.title ?: return@setOnMarkerClickListener false
                                //selectedCityGroups.clear()
                                val newCenter = GeoPoint(marker.position.latitude - 0.015, marker.position.longitude) // 緯度を少し減らす
                                mapView.controller.apply {
                                    animateTo(newCenter)
                                    setZoom(15.0)
                                }
                                showInfoWindow()
                                scope.launch { bottomSheetState.show() }
                                true
                            }
                        }
                    }
                    cityMarkers.forEach {overlays.add(it) }
                }
            }, modifier = Modifier.fillMaxSize())
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
