package com.example.droidsoftthird.composable.map.place

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.PlaceMapViewModel
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.PlaceMapBottomModal
import com.example.droidsoftthird.composable.shared.CommonLinearProgressIndicator
import com.example.droidsoftthird.composable.shared.SharedBackButton
import com.example.droidsoftthird.model.domain_model.EditedPlace

@Composable
fun PlaceMapScreen(
    viewModel: PlaceMapViewModel,
    confirmPlace: (EditedPlace?) -> Unit,
    navigateUp: () -> Unit
) {
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
        Column(modifier = Modifier.padding(4.dp)) {
            SharedBackButton(navigateUp)
            PlaceSearchComponent(searchByText, searchByCategory)
        }
        if (viewState.value.isLoading || isMapLoading) CommonLinearProgressIndicator()
    }
}
