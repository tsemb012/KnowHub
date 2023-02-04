package com.example.droidsoftthird

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.ViewPort
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val useCase: MapUseCase) : ViewModel() {

    //LoadStateで読み込みを管理。それ以外の動作はCompose内部で管理。それに合わせてメッセージを切り替える。

    val selectedType: MutableState<String> = mutableStateOf("restaurant")
    val selections: MutableState<List<String>> = mutableStateOf(listOf("restaurant", "cafe"))
    val tokyo = LatLng(35.681236, 139.767125)
    val query: MutableState<String> = mutableStateOf("")
    private val viewPort: MutableState<ViewPort> = mutableStateOf(ViewPort(null, null))
    val centerPoint: MutableState<LatLng> = mutableStateOf(tokyo)
    val radius: MutableState<Int> = mutableStateOf(500)
    val placesLoadState: MutableState<LoadState> = mutableStateOf(LoadState.Initialized)
    val placeDetailLoadState: MutableState<LoadState> = mutableStateOf(LoadState.Initialized)

    //TODO Flowで流してComibineするのが良いかも。
    //TODO Messageに詳細情報を含めて、モーダルを出現させるようにする。
    fun fetchPlaceDetail(placeId: String) {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            runCatching { useCase.fetchPlaceDetail(placeId) }
                .onSuccess {
                    it?.let { placeDetail -> placeDetailLoadState.value = LoadState.Loaded(placeDetail) }
                }
                .onFailure {
                    placeDetailLoadState.value = LoadState.Error(it)
                }
        }
        placeDetailLoadState.value = LoadState.Loading(job)
        job.start()
    }

    fun searchByText() {//TODO Markerの名前を変えた方が良いかもしれない。
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            runCatching {
                useCase.searchByText(query.value, centerPoint.value, selectedType.value, radius.value)
            }
                .onSuccess {
                    placesLoadState.value = LoadState.Loaded(it)
                }
                .onFailure { placesLoadState.value = LoadState.Error(it) }
        }
        placesLoadState.value = LoadState.Loading(job)
        job.start()
    }

    fun searchByPoi() {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            runCatching { useCase.searchByPoi(centerPoint.value, selectedType.value, radius.value) }
                .onSuccess { placesLoadState.value = LoadState.Loaded(it) }
                .onFailure { placesLoadState.value = LoadState.Error(it) }
        }
        placesLoadState.value = LoadState.Loading(job)
        job.start()
    }
    /*
    TODO 個別検索のユースケースは今のところ存在しないので置いておく。
    fun searchByIndividual() {
        viewModelScope.launch {
            runCatching { useCase.searchIndividualPlace(query.value, viewPort.value) }
                .onSuccess {  }
                .onFailure {  }
        }
    }
    */
    fun updateViewPoint(northEast: LatLng, southWest: LatLng) {
        viewPort.value = ViewPort(northEast, southWest)
    }
}
