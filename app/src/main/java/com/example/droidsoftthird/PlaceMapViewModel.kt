package com.example.droidsoftthird

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.Category
import com.example.droidsoftthird.model.domain_model.ViewPort
import com.example.droidsoftthird.model.domain_model.YolpSimplePlace
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.usecase.MapUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceMapViewModel @Inject constructor(private val useCase: MapUseCase) : ViewModel() {

    var viewState: PlaceMapViewState by mutableStateOf(PlaceMapViewState())

    fun fetchPlaceDetail(placeId: String) {
        launchDataLoad({ useCase.fetchPlaceDetail(placeId) }) { loadState ->
            viewState.copy(placeDetailLoadState = loadState)
        }
    }

    fun searchByText() {
        launchDataLoad({ useCase.searchByText(viewState.query, viewState.viewPort, viewState.centerPoint) }) { loadState ->
            viewState.copy(placesLoadState = loadState)
        }
    }

    fun searchByPoi() {
        launchDataLoad({ useCase.searchByPoi(viewState.viewPort, viewState.centerPoint, Category.CAFE) }) { loadState ->
            viewState.copy(placesLoadState = loadState)
        }
    }

    fun autoComplete() {
        launchDataLoad({ useCase.autoComplete(viewState.query, viewState.viewPort, viewState.centerPoint) }) { loadState ->
            viewState.copy(placesLoadState = loadState)
        }
    }

    fun reverseGeocode() {
        launchDataLoad({ useCase.reverseGeocode(viewState.centerPoint) }) { loadState ->
            viewState.copy(reverseGeocodeLoadState = loadState)
        }
    }

    fun updateViewPoint(northEast: LatLng, southWest: LatLng) {
        viewState = viewState.copy(viewPort = ViewPort(northEast, southWest))
    }

    private fun <T> launchDataLoad(
        block: suspend () -> T,
        stateUpdater: (LoadState) -> PlaceMapViewState
    ) {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            runCatching { block() }
                .onSuccess { viewState = it?.let { stateUpdater(LoadState.Loaded(it)) } ?: viewState }
                .onFailure { viewState = stateUpdater(LoadState.Error(it)) }
        }
        viewState = stateUpdater(LoadState.Loading(job))
    }
}


data class PlaceMapViewState(
    val selectedType: String = "restaurant",
    val selections: List<String> = listOf("restaurant", "cafe"),
    val centerPoint: LatLng = LatLng(35.681236, 139.767125),
    val radius: Int = 500,
    val viewPort: ViewPort = ViewPort(null, null),
    val query: String = "",
    val placesLoadState: LoadState = LoadState.Initialized,
    val placeDetailLoadState: LoadState = LoadState.Initialized,
    val reverseGeocodeLoadState: LoadState = LoadState.Initialized
) {
    val places = placesLoadState.getValueOrNull<List<YolpSimplePlace>>()
    val placeDetail = placeDetailLoadState.getValueOrNull<YolpSimplePlace>()
    val isLoading = placesLoadState is LoadState.Loading || placeDetailLoadState is LoadState.Loading || reverseGeocodeLoadState is LoadState.Loading
    val error = placesLoadState.getErrorOrNull() ?: placeDetailLoadState.getErrorOrNull() ?: reverseGeocodeLoadState.getErrorOrNull()
}
