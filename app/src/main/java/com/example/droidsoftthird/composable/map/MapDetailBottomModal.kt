package com.example.droidsoftthird.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun BottomModal(
    bottomSheetState: ModalBottomSheetState,
    content: @Composable () -> Unit = {}
) {
    //val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    //TODO 内部に入力情報を保持するように　→　修正されたら内部で修正するようにする。　→　修正されたら
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            Column {
                TextField(value = "", onValueChange = {})
                TextField(value = "", onValueChange = {})
                TextField(value = "", onValueChange = {})
                TextField(value = "", onValueChange = {})
                Row {
                    Button(onClick = { scope.launch { bottomSheetState.hide() } }) {
                        Text(text = "Close")
                    }
                    Button(onClick = { scope.launch { bottomSheetState.show() } }) {
                        Text(text = "Show")
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