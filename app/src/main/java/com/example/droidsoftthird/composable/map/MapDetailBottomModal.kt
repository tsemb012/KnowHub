package com.example.droidsoftthird.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.PlaceMapViewState
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.shared.DescriptionItem
import com.example.droidsoftthird.composable.shared.SharedDescriptions
import com.example.droidsoftthird.model.domain_model.*
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
            var memo by remember { mutableStateOf("") }

            val descriptionItems = listOf(
                DescriptionItem(Icons.Filled.Phone, viewState.value.placeDetail?.tel ?: "", 1),
                DescriptionItem(Icons.Filled.LocationOn, viewState.value.placeDetail?.formattedAddress ?: "", 2),
                DescriptionItem(Icons.Filled.Language, viewState.value.placeDetail?.url ?: "", 2, true),
            )

            Column {
                SharedDescriptions(
                    title = viewState.value.placeDetail?.name ?: "",
                    itemList = descriptionItems
                )
                EditableListItem(
                    label = stringResource(R.string.map_memo),
                    value = memo,
                    onTextChanged = { memo = it })
                Row {
                    Button(onClick = { scope.launch { bottomSheetState.hide() } }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Button(onClick = { onConfirm(viewState.value.placeDetail?.toEditedPlace()?.copy(memo = memo)) }) {
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