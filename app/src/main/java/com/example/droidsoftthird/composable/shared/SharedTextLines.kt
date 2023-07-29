package com.example.droidsoftthird.composable.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    descriptionTextColor: Color = Color.DarkGray,
    hasSpace: Boolean = false,
) {
    val titleTextStyle = when (titleTextSize) {
        TextSize.SMALL -> MaterialTheme.typography.body2
        TextSize.MEDIUM -> MaterialTheme.typography.h6
        TextSize.MED_LARGE -> MaterialTheme.typography.h5
        TextSize.LARGE -> MaterialTheme.typography.h4
    }

    val descriptionTextStyle = when (descriptionTextSize) {
        TextSize.SMALL -> MaterialTheme.typography.body2
        TextSize.MEDIUM -> MaterialTheme.typography.h6
        TextSize.MED_LARGE -> MaterialTheme.typography.h5
        TextSize.LARGE -> MaterialTheme.typography.h4
    }

    Column (modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = TextStyle(fontWeight = FontWeight.Bold ,fontSize = titleTextStyle.fontSize, color = Color.DarkGray),
        )
        Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
        Row {
            if (hasSpace) {
                Spacer(modifier = androidx.compose.ui.Modifier.width(16.dp))
            }
            Text(
                text = text,
                style = TextStyle(fontSize = descriptionTextStyle.fontSize, color = descriptionTextColor),
            )
        }

    }
}
