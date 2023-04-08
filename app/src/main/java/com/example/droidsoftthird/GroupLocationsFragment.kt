package com.example.droidsoftthird

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Copy
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.OverlayItem

@AndroidEntryPoint
class GroupLocationsFragment:Fragment() {

    private val viewModel:GroupLocationsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osmdroid", 0))
        // osmdroid configuration
        //Configuration.getInstance().load(context, context?.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                OSMMapView()
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OSMMapView() {
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val selectedCityGroups = remember { mutableStateListOf<StudyGroup>() }



    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            // Show the list of groups for the selected city in the bottom sheet
            //GroupList(groups = selectedCityGroups)
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

                    // 緯度と経度を設定します。
                    val latitude = 35.6895 // 東京の緯度
                    val longitude = 139.6917 // 東京の経度
                    val zoomLevel = 12.0 // ズームレベル（1〜20）



                    controller.apply {
                        setZoom(zoomLevel)
                        setCenter(GeoPoint(latitude, longitude))
                    }

                    val studyGroups = listOf(
                        // Dummy data
                        StudyGroup(id = 1, location = GeoPoint(35.6895, 139.6917), cityName = "Tokyo"),
                        StudyGroup(id = 2, location = GeoPoint(35.6895, 139.6917), cityName = "Tokyo"),
                        StudyGroup(id = 3, location = GeoPoint(34.693738, 135.502165), cityName = "Osaka"),
                        StudyGroup(id = 4, location = GeoPoint(34.693738, 135.502165), cityName = "Osaka"),
                        StudyGroup(id = 5, location = GeoPoint(34.693738, 135.502165), cityName = "Osaka"),
                        StudyGroup(id = 6, location = GeoPoint(43.062096, 141.354376), cityName = "Sapporo")
                    )

                    val cityToGroupCount = studyGroups.groupBy { it.cityName }.mapValues { it.value.size }
                    val markerClickListener = object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                        override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                            return false
                        }

                        override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                            val city = item?.title ?: return false
                            selectedCityGroups.clear()
                            selectedCityGroups.addAll(studyGroups.filter { it.cityName == city })
                            //bottomSheetState.show()
                            return true
                        }
                    }

/*                    val cityMarkers = cityToGroupCount.map { (city, count) ->
                        val location = studyGroups.first { it.cityName == city }.location
                        val marker = OverlayItem(city, "$city: $count groups", location)
                        marker.setMarker(ContextCompat.getDrawable(context, R.drawable.ic_map_marker))
                        marker
                    }*/

                    /*val markerOverlay = ItemizedIconOverlay(context, cityMarkers, markerClickListener)
                    overlays.add(markerOverlay)

                    // Add a MapEventsOverlay to detect when the map is tapped
                    val mapEventsReceiver = object : MapEventsReceiver {
                        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                            bottomSheetState.hide()
                            return false
                        }

                        override fun longPressHelper(p: GeoPoint?): Boolean {
                            return false
                        }
                    }*/
                    //overlays.add(MapEventsOverlay(mapEventsReceiver))
                }
            }, modifier = Modifier.fillMaxSize())
        }
    }
}

/*@Composable
fun GroupList(groups: List<StudyGroup>) {
    // Display the list of groups in the selected city
    LazyColumn {
        items(groups) { group ->
            Text(text = "Group ${group.id}: ${group.cityName}")
        }
    }
}*/

data class StudyGroup(
    val id: Int,
    val location: GeoPoint,
    val cityName: String
)