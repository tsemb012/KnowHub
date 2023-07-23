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
    Column(Modifier.padding(horizontal = 28.dp, vertical = 16.dp)) {
        SearchBox(searchByText, autoComplete)
        Box {
            ChipGroup(Category.values()) { category -> searchByCategory(category) }
            LazyColumn(modifier = Modifier.background(color = colorResource(id = R.color.base_100))) {
                viewState.value.autoCompleteItems?.let { list ->
                    items(list.size) {
                        val keyboardController = LocalSoftwareKeyboardController.current
                        Column(Modifier.clickable(onClick = {
                            keyboardController?.hide()
                            updateViewState(viewState.value.copy(
                                autoCompleteLoadState = LoadState.Initialized,
                                currentPoint = LatLng(list[it].location.lat, list[it].location.lng)
                            //TODO 場所を移動
                            //TODO ピンを指す
                            //TODO BottomSheetを出す、
                            ))
                            fetchPlaceDetail(list[it].id)
                        })) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Start,
                            ) {
                                Text(
                                    text = viewState.value.currentPoint?.let { currentPoint -> "${list[it].calculateKiloMeter(currentPoint)} km" } ?: "N/A",
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                )
                                Text(
                                    text = list[it].name,
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




        //TOOD AutoCompleteの実装はSearchBoxとまとめて行う。
        /*modifier = Modifier
                    .height(56.dp)
                    .padding(top = 16.dp))*/
    }
}