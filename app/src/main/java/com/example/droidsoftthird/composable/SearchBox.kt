package com.example.droidsoftthird.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.droidsoftthird.R

@Composable
fun SearchBox(callback: (String) -> Unit) {
    val query = remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(50), // for rounded corners
        color = colorResource(id = R.color.base_100),
        elevation = 4.dp, // to give a raised effect
    ) {
        TextField(
            modifier = Modifier
                .height(51.dp) // adjust the height of TextField
                .padding( horizontal = 12.dp)
                .fillMaxWidth(),
            value = query.value,
            onValueChange = { query.value = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, // to ensure the TextField is transparent and takes Surface color
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { callback(query.value) }),
        )
    }
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val query = remember { mutableStateOf("") }
    MaterialTheme {
        SearchBox() {}
    }
}
