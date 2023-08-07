package com.tsemb.droidsoftthird.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tsemb.droidsoftthird.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchBox(
    onSearch: (String) -> Unit,
    onAutoComplete: (String) -> Unit,
    clearSearch: () -> Unit?
) {
    val query = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val currentJob = remember { mutableStateOf<Job?>(null) }

    Surface(
        shape = RoundedCornerShape(50), // for rounded corners
        color = colorResource(id = R.color.base_100),
        elevation = 4.dp, // to give a raised effect
    ) {
        TextField(
            modifier = Modifier
                .height(51.dp) // adjust the height of TextField
                .fillMaxWidth(),
            value = query.value,
            onValueChange = { newValue ->
                query.value = newValue

                currentJob.value?.cancel()
                currentJob.value = scope.launch {
                    delay(800) // wait for 800ms
                    if (query.value.isNotBlank()) onAutoComplete(newValue)
                    else clearSearch()
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (query.value.isNotBlank()) {
                    IconButton(onClick = {
                        query.value = ""
                        clearSearch()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, // to ensure the TextField is transparent and takes Surface color
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = colorResource(id = R.color.primary_dark), // Replace with your cursor color
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch(query.value)
                clearSearch()
            }),
        )
    }
}





@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val query = remember { mutableStateOf("") }
    MaterialTheme {
    }
}
