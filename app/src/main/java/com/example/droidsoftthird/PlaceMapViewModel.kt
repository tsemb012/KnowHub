package com.example.droidsoftthird

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.Category
import com.example.droidsoftthird.model.domain_model.ViewPort
import com.example.droidsoftthird.model.domain_model.YolpDetailPlace
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

    private var _viewState: MutableState<PlaceMapViewState> = mutableStateOf(PlaceMapViewState())
    val viewState: State<PlaceMapViewState> get() = _viewState

    fun fetchPlaceDetail(placeId: String) {
        launchDataLoad({ useCase.fetchPlaceDetail(placeId) }) { loadState ->
            _viewState.value = _viewState.value.copy(placeDetailLoadState = loadState)
        }
    }

    fun searchByText(query: String) {
        launchDataLoad({ useCase.searchByText(query, viewState.value.viewPort, viewState.value.centerPoint) }) { loadState ->
            _viewState.value = _viewState.value.copy(placesLoadState = loadState)
        }
    }

    fun searchByCategory(category: Category) {
        launchDataLoad({ useCase.searchByCategory(viewState.value.viewPort, viewState.value.centerPoint, category) }) { loadState ->
            _viewState.value = _viewState.value.copy(placesLoadState = loadState)
        }
    }

    fun autoComplete(query: String) {
        launchDataLoad({ useCase.autoComplete(query, viewState.value.viewPort, viewState.value.centerPoint) }) { loadState ->
            _viewState.value = _viewState.value.copy(placesLoadState = loadState)
        }
    }

    fun reverseGeocode() {
        launchDataLoad({ useCase.reverseGeocode(viewState.value.centerPoint) }) { loadState ->
            _viewState.value = _viewState.value.copy(reverseGeocodeLoadState = loadState)
        }
    }

    fun updateViewState(state: PlaceMapViewState) {
        _viewState.value = state
    }

    private fun <T> launchDataLoad(
        block: suspend () -> T,
        stateUpdater: (LoadState) -> Unit
    ) {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            runCatching { block() }
                .onSuccess { it?.let { stateUpdater(LoadState.Loaded(it)) } }
                .onFailure { stateUpdater(LoadState.Error(it)) }
        }
        stateUpdater(LoadState.Loading(job))
        job.start()
    }
}


data class PlaceMapViewState(
    val selectedType: String = "restaurant",
    val selections: List<String> = listOf("restaurant", "cafe"),
    val centerPoint: LatLng = LatLng(35.681236, 139.767125),
    val radius: Int = 500,
    val viewPort: ViewPort = ViewPort(null, null),
    val placesLoadState: LoadState = LoadState.Initialized,
    val placeDetailLoadState: LoadState = LoadState.Initialized,
    val reverseGeocodeLoadState: LoadState = LoadState.Initialized
) {
    val places = placesLoadState.getValueOrNull<List<YolpSimplePlace>>()
    val placeDetail = placeDetailLoadState.getValueOrNull<YolpDetailPlace>()
    val isLoading = placesLoadState is LoadState.Loading || placeDetailLoadState is LoadState.Loading || reverseGeocodeLoadState is LoadState.Loading
    val error = placesLoadState.getErrorOrNull() ?: placeDetailLoadState.getErrorOrNull() ?: reverseGeocodeLoadState.getErrorOrNull()
}
