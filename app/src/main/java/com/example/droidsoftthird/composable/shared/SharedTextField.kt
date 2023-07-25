package com.example.droidsoftthird.composable.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SharedTextField(
    title: String,
    text: String,
    isEditable : Boolean,
    hint: String = "",
    onTextChanged: (String) -> Unit = {}
) {
    Column {
        Text(
            text = title,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        TextField(
            value = text,
            enabled = isEditable,
            onValueChange = { onTextChanged(it) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            textStyle = TextStyle(color = Color.DarkGray),
            placeholder = {
                Text(
                    text = hint,
                    style = TextStyle(color = Color.Gray)
                )
            }
        )
    }
}