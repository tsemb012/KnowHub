package com.example.droidsoftthird.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tsemb.droidsoftthird.PlaceMapViewState
import com.example.droidsoftthird.R
import com.tsemb.droidsoftthird.composable.shared.DescriptionItem
import com.tsemb.droidsoftthird.composable.shared.SharedDescriptions
import com.tsemb.droidsoftthird.composable.shared.SharedTextField
import com.example.droidsoftthird.model.domain_model.*
import com.tsemb.droidsoftthird.model.domain_model.EditedPlace
import com.tsemb.droidsoftthird.model.domain_model.YolpSinglePlace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun PlaceMapBottomModal(
    viewState: State<PlaceMapViewState>,
    updateViewState: KFunction1<PlaceMapViewState, Unit>,
    bottomSheetState: ModalBottomSheetState,
    onConfirm: (EditedPlace?) -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    var isOpenable by remember { mutableStateOf(false) } // Initialize isOpenable to false
    var singlePlace: YolpSinglePlace? = null

    LaunchedEffect(viewState.value.singlePlace) { // Whenever placeDetail changes, update isOpenable
        isOpenable = viewState.value.singlePlace != null
        if (isOpenable) {
            scope.launch { bottomSheetState.show() }
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            var memo by remember { mutableStateOf("") }
            var placeName by remember { mutableStateOf("") }
            Column(modifier = Modifier.padding(16.dp)) {
                when (viewState.value.singlePlace) {
                    is YolpSinglePlace.DetailPlace -> {
                        singlePlace = viewState.value.singlePlace
                        val place = singlePlace as YolpSinglePlace.DetailPlace
                        val descriptionItems = listOf(
                            DescriptionItem(
                                Icons.Filled.Phone,
                                place.tel ?: "",
                                1
                            ),
                            DescriptionItem(
                                Icons.Filled.LocationOn,
                                place.address,
                                2
                            ),
                            DescriptionItem(
                                Icons.Filled.Language,
                                place.url ?: "",
                                2,
                                true
                            ),
                        )
                        SharedDescriptions(
                            title = place.name ?: "",
                            itemList = descriptionItems
                        )
                    }
                    is YolpSinglePlace.ReverseGeocode -> {

                        singlePlace = viewState.value.singlePlace

                        val place = singlePlace as YolpSinglePlace.ReverseGeocode
                        SharedTextField("住所", place.address, false)
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))
                        SharedTextField(title = "場所名", text = placeName, isEditable = true, hint = "場所の名前を入力してください") { placeName = it }
                    }
                    else -> {
                        Text(text = "No place")}
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                SharedTextField(title = stringResource(R.string.map_memo), text = memo,isEditable = true, hint = "メモを入力してください") { memo = it }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(40.dp)
                            .padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.gray),
                            contentColor = Color.DarkGray
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        ),
                        onClick = { scope.launch { bottomSheetState.hide() } }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Button(
                        modifier = Modifier
                            .width(160.dp)
                            .height(40.dp)
                            .padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.primary_dark),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        ),
                        onClick = {
                            if (singlePlace != null) onConfirm(
                                singlePlace!!.toEditedPlace(memo, placeName)
                            )
                        }) {
                        Text(text = "場所を追加")
                    }
                }
            }
        },
        content = content
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SharedConfirmButtons(
    scope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
) {

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
fun ListItemPreview() {
    ListItem(label = "Name", value = "value")
}