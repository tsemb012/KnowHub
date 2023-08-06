package com.tsemb.droidsoftthird.composable.shared

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
    titleTextSize: TextSize = TextSize.MEDIUM,
    fieldTextSize: TextSize = TextSize.MEDIUM,
    hint: String = "",
    onTextChanged: (String) -> Unit = {}
) {
    val titleTextStyle = when (titleTextSize) {
        TextSize.SMALL -> MaterialTheme.typography.body2
        TextSize.MEDIUM -> MaterialTheme.typography.h6
        TextSize.MED_LARGE -> MaterialTheme.typography.h5
        TextSize.LARGE -> MaterialTheme.typography.h4
    }

    val fieldTextStyle = when (fieldTextSize) {
        TextSize.SMALL -> MaterialTheme.typography.body2
        TextSize.MEDIUM -> MaterialTheme.typography.h6
        TextSize.MED_LARGE -> MaterialTheme.typography.h5
        TextSize.LARGE -> MaterialTheme.typography.h4
    }

    Column {
        Text(
            text = title,
            style = TextStyle(fontWeight = FontWeight.Bold ,fontSize = titleTextStyle.fontSize, color = Color.DarkGray),
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
            textStyle = TextStyle(fontSize = fieldTextStyle.fontSize, color = Color.DarkGray),
            placeholder = {
                Text(
                    text = hint,
                    style = TextStyle(fontSize = fieldTextStyle.fontSize, color = Color.Gray)
                )
            }
        )
    }
}

enum class TextSize {
    SMALL,
    MEDIUM,
    MED_LARGE,
    LARGE
}
