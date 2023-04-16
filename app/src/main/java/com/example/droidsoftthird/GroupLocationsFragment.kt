package com.example.droidsoftthird

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.example.droidsoftthird.model.domain_model.ApiGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@AndroidEntryPoint
class GroupLocationsFragment:Fragment() {

    private val viewModel:GroupLocationsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLocationPermission()
        viewModel.getCountByArea()
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osmdroid", 0))
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                OSMMapView(
                    viewModel,
                    this@GroupLocationsFragment,
                    ::navigateToGroupDetail
                )
            }
        }
    }

    fun navigateToGroupDetail(groupId: String) {
        this.findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToGroupDetailFragment(groupId)
        )
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OSMMapView(
    viewModel: GroupLocationsViewModel,
    fragment: GroupLocationsFragment,
    navigateToDetail: (String) -> Unit
) {

    val uiModel = viewModel.uiModel.observeAsState()


    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()



    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            val lazyPagingGroups = (uiModel.value?.groupsBySelectedArea ?: emptyFlow()).collectAsLazyPagingItems()
            GroupList(
                lazyPagingGroups,
                navigateToDetail,
            )
        }
    ) {
        MainScreenContent(
            uiModel,
            fragment,
            bottomSheetState,
            scope
        ) { code:Int, category:String ->  viewModel.getGroupsByArea(code, category) }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreenContent(
        uiModel: State<GroupLocationsUiModel?>,
        fragment: GroupLocationsFragment,
        bottomSheetState: ModalBottomSheetState,
        scope: CoroutineScope,
        getGroups: (Int, String) -> Unit
) {


    val groupCountByCity = uiModel.groupCountByArea?.filter { it.category == "city" }
    val groupCountByPrefecture = mutableListOf(uiModel.groupCountByArea?.filter { it.category == "prefecture" })

    Log.d("GroupLocationsFragment", "groupCountByCity2: ${groupCountByCity}2")

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(

            update = { mapView ->

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
                            val newCenter = GeoPoint(marker.position.latitude - 0.015, marker.position.longitude) // 緯度を少し減らす
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
                cityMarkers?.forEach {mapView.overlays.add(it) }
            },

            factory = { context ->

            MapView(context).apply {

                setTileSource(TileSourceFactory.MAPNIK)
                setBuiltInZoomControls(true)
                setMultiTouchControls(true)

                val locationManager = fragment.requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    val defaultTokyoLocation = GeoPoint(35.681236, 139.767125)
                    currentLocation.let {
                        controller.apply {
                            setZoom(12)
                            setCenter(GeoPoint(it?.latitude ?: defaultTokyoLocation.latitude, it?.longitude ?: defaultTokyoLocation.longitude))
                        }
                        val currentLocationMarker = Marker(this).apply {
                            position = GeoPoint(it?.latitude ?: defaultTokyoLocation.latitude, it?.longitude  ?: defaultTokyoLocation.longitude)
                            icon = createBlueDotDrawable(fragment.requireContext())
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        }
                        overlays.add(currentLocationMarker)
                    }
                }
            }
        }, modifier = Modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupList(groups: LazyPagingItems<ApiGroup>, navigateToDetail: (String) -> Unit, ) {
    var refreshing by remember { mutableStateOf(false) }
    fun refresh () {groups.refresh()}

    val state = rememberPullRefreshState(refreshing, ::refresh)
    Box(modifier = Modifier.pullRefresh(state)) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            items(groups) {
                it?.let { group -> GroupListItem(group, navigateToDetail) }
            }
        }
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupListItem(group: ApiGroup, navigateToDetail: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 8.dp,
        onClick = { group.groupId?.let { navigateToDetail(it) } }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AsyncImage(model = group.storageRef,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = group.groupName, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = group.groupIntroduction, style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${group.prefecture} ${group.city}",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.align(Alignment.End)
                )
            }
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

private fun createBlueDotDrawable(context: Context): Drawable {
    val circleDrawable = ShapeDrawable(OvalShape())
    circleDrawable.paint.color = Color.BLUE
    circleDrawable.intrinsicWidth = 24
    circleDrawable.intrinsicHeight = 24
    circleDrawable.setBounds(0, 0, circleDrawable.intrinsicWidth, circleDrawable.intrinsicHeight)
    return circleDrawable
}
