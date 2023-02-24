package com.example.droidsoftthird.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchBox(query: MutableState<String>, callback: () -> Unit,){
    Row(
        modifier = Modifier.height(56.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        TextField(
            modifier = Modifier,
            value = query.value,
            onValueChange = { query.value = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            label = { Text("Search") }
        )
        Button(
            modifier = Modifier.fillMaxHeight(),
            onClick = { callback() }
        ) {
            Text("Search")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val query = remember { mutableStateOf("") }
    MaterialTheme {
        SearchBox(query = query , callback = {})
    }
}
