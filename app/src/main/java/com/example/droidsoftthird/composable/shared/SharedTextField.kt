package com.example.droidsoftthird.composable.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
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
    isEditable: Boolean,
    textSize: TextSize = TextSize.MEDIUM,
    hint: String = "",
    onTextChanged: (String) -> Unit = {}
) {
    val chosenTextStyle = when (textSize) {
        TextSize.SMALL -> MaterialTheme.typography.body2
        TextSize.MEDIUM -> MaterialTheme.typography.h6
        TextSize.LARGE -> MaterialTheme.typography.h4
    }

    Column {
        Text(
            text = title,
            style = TextStyle(fontWeight = FontWeight.Bold ,fontSize = chosenTextStyle.fontSize,  color = Color.DarkGray),
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
            textStyle = TextStyle(fontSize = chosenTextStyle.fontSize, color = Color.DarkGray),
            placeholder = {
                Text(
                    text = hint,
                    style = TextStyle(fontSize = chosenTextStyle.fontSize, color = Color.Gray)
                )
            }
        )
    }
}

enum class TextSize {
    SMALL,
    MEDIUM,
    LARGE
}
