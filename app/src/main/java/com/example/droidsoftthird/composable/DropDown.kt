package com.example.droidsoftthird.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DropDown(items : MutableState<List<String>>, selectedItem : MutableState<String>) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)) {
        Text(
            text = selectedItem.value,
            modifier = Modifier.fillMaxWidth().clickable(onClick = { expanded = true }).background(Color.Gray)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth().background(Color.White)
        ) {
            items.value.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedItem.value = s
                    expanded = false
                }) { Text(text = s) }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun DefaultDropDownPreview() {
    val items = mutableStateOf(listOf("A", "B", "C"))
    val selectedItem = mutableStateOf("A")
    DropDown(items, selectedItem)
}
