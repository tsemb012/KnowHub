package com.example.droidsoftthird.composable.map.place

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.PlaceMapViewState
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.ChipGroup
import com.example.droidsoftthird.composable.SearchBox
import com.example.droidsoftthird.model.domain_model.Category
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlaceSearchComponent(
    viewState: State<PlaceMapViewState>,
    updateViewState: (PlaceMapViewState) -> Unit,
    searchByText: (query: String) -> Unit,
    searchByCategory: (category: Category) -> Unit,
    autoComplete: (query: String) -> Unit,
    fetchPlaceDetail: (placeId: String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val clearSearch = {
        updateViewState(viewState.value.copy(
            autoCompleteLoadState = LoadState.Initialized,
        ))
        keyboardController?.hide()
    }
    Column(Modifier.padding(horizontal = 28.dp, vertical = 8.dp)) {
        SearchBox(searchByText, autoComplete, clearSearch)
        Box {
            ChipGroup(Category.values()) { category -> searchByCategory(category) }
            AutoCompleteList(viewState, updateViewState, keyboardController, fetchPlaceDetail)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AutoCompleteList(
    viewState: State<PlaceMapViewState>,
    updateViewState: (PlaceMapViewState) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    fetchPlaceDetail: (placeId: String) -> Unit,
) {
    LazyColumn(modifier = Modifier.background(color = colorResource(id = R.color.base_100))) {
        viewState.value.autoCompleteItems?.let { list ->
            // Sort list by distance
            val sortedList = list.sortedBy { list -> viewState.value.currentPoint?.let { list.calculateKiloMeter(it) } }

            items(sortedList.size) {
                Column(Modifier.clickable(onClick = {
                    updateViewState(
                        viewState.value.copy(
                            autoCompleteLoadState = LoadState.Initialized,
                        )
                    )
                    keyboardController?.hide()
                    fetchPlaceDetail(sortedList[it].id)
                })) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = viewState.value.currentPoint?.let { currentPoint ->
                                "${sortedList[it].calculateKiloMeter(currentPoint)} km"
                            } ?: "N/A",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                        )
                        Text(
                            text = sortedList[it].name,
                            modifier = Modifier.weight(4f),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                    }
                    Divider()
                }
            }
        }
    }
}