package com.example.droidsoftthird.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.PlaceMapViewState
import com.example.droidsoftthird.R
import com.example.droidsoftthird.model.domain_model.*
import com.example.droidsoftthird.model.presentation_model.LoadState
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun PlaceMapBottomModal(
    viewState: State<PlaceMapViewState>,
    updateViewState: KFunction1<PlaceMapViewState, Unit>,
    onConfirm: (EditedPlace?) -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    var isOpenable by remember { mutableStateOf(false) } // Initialize isOpenable to false

    LaunchedEffect(viewState.value.placeDetail) { // Whenever placeDetail changes, update isOpenable
        isOpenable = viewState.value.placeDetail?.toEditedPlace() != null
        if (isOpenable) {
            scope.launch { bottomSheetState.show() }
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            val editedPlace = viewState.value.placeDetail?.toEditedPlace()
            var editedPlaceDetail by remember { mutableStateOf<EditedPlace?>(editedPlace) }

            Column {
                ListItem(label = stringResource(R.string.map_place_name), value = viewState.value.placeDetail?.name ?: "")
                ListItem(label = stringResource(R.string.map_address), value = editedPlaceDetail?.formattedAddress ?: "")
                ListItem(label = stringResource(R.string.map_category), value = editedPlaceDetail?.category ?: "")
                EditableListItem(label = stringResource(R.string.map_memo), value = editedPlaceDetail?.memo?: "", onTextChanged = { editedPlaceDetail = editedPlaceDetail?.copy(memo = it) })
                Row {
                    Button(onClick = { scope.launch { bottomSheetState.hide() } }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Button(onClick = { onConfirm(editedPlaceDetail) }) {
                        Text(text = stringResource(id = R.string.general_confirm))
                    }
                }
            }
        },
        content = content
    )
}

@Composable
fun ListItem (label: String, value: String) {
    Row {
        Text(text = label, modifier = Modifier
            .height(60.dp)
            .width(100.dp))
        Text(text = value, modifier = Modifier
            .height(60.dp)
            .fillMaxWidth())
    }
}

@Composable
fun EditableListItem (label: String, value: String, onTextChanged: (String) -> Unit) {
    Row {
        Text(text = label, modifier = Modifier
            .height(60.dp)
            .width(100.dp))
        TextField(value = value, modifier = Modifier.fillMaxWidth(), onValueChange = { onTextChanged(it) })
    }
}

@Preview
@Composable
fun BottomModalPreview() {
    //BottomModal(isExpanded = true)
}

@Preview
@Composable
fun ListItemPreview() {
    ListItem(label = "Name", value = "value")
}