package com.example.droidsoftthird.composable

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.droidsoftthird.model.domain_model.PlaceDetail
import com.example.droidsoftthird.model.presentation_model.LoadState
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun BottomModal(
    placeDetailLoadState: MutableState<LoadState>,
    onConfirm: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    //TODO 内部に入力情報を保持するように　→　修正されたら内部で修正するようにする。　→　修正されたら
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {

            when (placeDetailLoadState.value) {
                is LoadState.Error -> {
                    Toast.makeText(LocalContext.current, placeDetailLoadState.value.getErrorOrNull().toString(), Toast.LENGTH_SHORT).show()
                    placeDetailLoadState.value = LoadState.Initialized
                }
                is LoadState.Loaded<*> -> {
                    placeDetailLoadState.value.getValueOrNull<PlaceDetail>()?.let {
                        scope.launch { bottomSheetState.show() }
                        name = it.name
                        address = it.formattedAddress ?: ""
                    }
                    placeDetailLoadState.value = LoadState.Initialized
                }
            }
            Column {
                TextField(value = name, onValueChange = {name = it})
                TextField(value = address, onValueChange = {address = it})
                TextField(value = "", onValueChange = {})
                TextField(value = "", onValueChange = {})
                Row {
                    Button(onClick = { scope.launch { bottomSheetState.hide() } }) {
                        Text(text = "Close")
                    }
                    Button(onClick = { scope.launch { bottomSheetState.show() } }) {
                        Text(text = "Show")
                        onConfirm()
                    }
                }
            }
        },
        content = content
    )
}

@Preview
@Composable
fun BottomModalPreview() {
    //BottomModal(isExpanded = true)
}