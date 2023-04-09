package com.example.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem

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

                    // 緯度と経度を設定します。
                    val latitude = 35.6895 // 東京の緯度
                    val longitude = 139.6917 // 東京の経度
                    val zoomLevel = 12.0 // ズームレベル（1〜20）



                    controller.apply {
                        setZoom(zoomLevel)
                        setCenter(GeoPoint(latitude, longitude))
                    }

                    val markerClickListener = object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                        override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                            return false
                        }

                        override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                            val city = item?.title ?: return false
                            //selectedCityGroups.clear()
                            //bottomSheetState.show()
                            return true
                        }
                    }

                    val cityMarkers = groupCountByCity.mapNotNull { groupCount ->
                        val city = groupCount.cityName ?: return@mapNotNull null
                        val location = GeoPoint(groupCount.latitude, groupCount.longitude)
                        val item = OverlayItem(city, "${city}: ${groupCount.groupCount} groups", location)
                        item.setMarker(ContextCompat.getDrawable(context, R.drawable.marker_default_focused_base))
                        item
                    }


                    val markerOverlay = ItemizedIconOverlay(context, cityMarkers, markerClickListener)
                    overlays.add(markerOverlay)

                }
            }, modifier = Modifier.fillMaxSize())
        }
    }
}
