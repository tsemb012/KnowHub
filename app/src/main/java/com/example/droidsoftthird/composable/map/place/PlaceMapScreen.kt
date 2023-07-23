package com.example.droidsoftthird.composable.map.place

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.droidsoftthird.PlaceMapViewModel
import com.example.droidsoftthird.composable.PlaceMapBottomModal
import com.example.droidsoftthird.composable.shared.CommonLinearProgressIndicator
import com.example.droidsoftthird.model.domain_model.EditedPlace

@Composable
fun PlaceMapScreen(viewModel: PlaceMapViewModel, confirmPlace: (EditedPlace?) -> Unit) {
    val viewState = viewModel.viewState
    val updateViewState = viewModel::updateViewState
    val fetchPlaceDetail = viewModel::fetchPlaceDetail
    val searchByText = viewModel::searchByText
    val searchByCategory = viewModel::searchByCategory

    val _isMapLoading = remember { mutableStateOf(true) }
    val isMapLoading = _isMapLoading.value

    PlaceMapBottomModal(
        viewState = viewState,
        updateViewState = updateViewState,
        onConfirm = { confirmPlace(it) }
    ) {
        GoogleMapScreen(viewState, updateViewState, fetchPlaceDetail) { _isMapLoading.value = false }
        PlaceSearchComponent(searchByText, searchByCategory)
        if (viewState.value.isLoading || isMapLoading) CommonLinearProgressIndicator()
    }
}

