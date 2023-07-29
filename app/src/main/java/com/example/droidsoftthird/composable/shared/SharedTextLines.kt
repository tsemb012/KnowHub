package com.example.droidsoftthird.composable.shared

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SharedTextLines(
    title: String,
    text: String,
    titleTextSize: TextSize = TextSize.MEDIUM,
    descriptionTextSize: TextSize = TextSize.MEDIUM,
) {
    val titleTextStyle = when (titleTextSize) {
        TextSize.SMALL -> MaterialTheme.typography.body2
        TextSize.MEDIUM -> MaterialTheme.typography.h6
        TextSize.LARGE -> MaterialTheme.typography.h4
    }

    val descriptionTextStyle = when (descriptionTextSize) {
        TextSize.SMALL -> MaterialTheme.typography.body2
        TextSize.MEDIUM -> MaterialTheme.typography.h6
        TextSize.LARGE -> MaterialTheme.typography.h4
    }

    Column {
        Text(
            text = title,
            style = TextStyle(fontWeight = FontWeight.Bold ,fontSize = titleTextStyle.fontSize, color = Color.DarkGray),
        )
        Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
        Text(
            text = text,
            style = TextStyle(fontSize = descriptionTextStyle.fontSize, color = Color.Gray),
        )
    }
}
