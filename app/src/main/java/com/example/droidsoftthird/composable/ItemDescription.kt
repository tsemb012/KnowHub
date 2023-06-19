package com.example.droidsoftthird.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ItemDescription(
    title: String,
    itemList: List<Pair<ImageVector, String>>,
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            color = Color.DarkGray,
            maxLines = 1
        )

        Column(modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)) {

            itemList.forEach { (icon, text) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterVertically),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = text,
                        style = MaterialTheme.typography.body1,
                        color = Color.DarkGray,
                        maxLines = 2
                    )
                }
            }
        }
    }
}