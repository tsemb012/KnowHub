package com.example.droidsoftthird

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.Place
import com.example.droidsoftthird.model.domain_model.ViewPort
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val useCase: MapUseCase) : ViewModel() {

    val selectedType: MutableState<String> = mutableStateOf("restaurant")
    val selections: MutableState<List<String>> = mutableStateOf(listOf("restaurant", "cafe"))
    val tokyo = LatLng(35.681236, 139.767125)
    val query: MutableState<String> = mutableStateOf("")
    private val viewPort: MutableState<ViewPort> = mutableStateOf(ViewPort(null, null))
    val centerPoint: MutableState<LatLng> = mutableStateOf(tokyo)
    val places: MutableState<List<Place>> = mutableStateOf(listOf())
    val radius: MutableState<Int> = mutableStateOf(500)
    val messages = mutableStateOf("")

    //TODO Messageに詳細情報を含めて、モーダルを出現させるようにする。

    fun onMarkerClick(placeId: String) {
        viewModelScope.launch {
            runCatching { useCase.fetchPlaceDetail(placeId) }
                .onSuccess { messages.value = it.toString() }
                .onFailure { messages.value = it.message ?: "Unknown error" }
        }
    }


    fun searchByText() {//TODO Markerの名前を変えた方が良いかもしれない。
        viewModelScope.launch {
            runCatching {
                //useCase.searchIndividualPlace(query.value, viewPort.value)
                useCase.searchByText(query.value, centerPoint.value, selectedType.value, radius.value)
            }
                .onSuccess {
                    places.value = it
                    Log.d("tsemb012-2", it.toString())
                }
                .onFailure { messages.value = it.message ?: "Unknown error" }
        }
    }

    fun searchByPoi() {
        viewModelScope.launch {
            runCatching { useCase.searchByPoi(centerPoint.value, selectedType.value, radius.value) }
                .onSuccess { places.value = it
                    Log.d("tsemb012-2", it.toString())
                }
                .onFailure { messages.value = it.message ?: "Unknown error"
                    Log.d("tsemb012-３", it.toString())
                }
        }
    }

    fun updateViewPoint(northEast: LatLng, southWest: LatLng) {
        viewPort.value = ViewPort(northEast, southWest)
    }


    fun fetchPlaceDetail(placeId: String) {
        viewModelScope.launch {
            runCatching { useCase.fetchPlaceDetail(placeId) }
                .onSuccess { /*messages = mutableStateOf(it.name)*/ }
                .onFailure { /*messages = mutableStateOf(it.message ?: "")*/ }
        }
    }
}
