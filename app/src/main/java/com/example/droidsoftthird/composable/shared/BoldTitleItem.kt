package com.example.droidsoftthird.composable.shared

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BoldTitleItem(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        color = Color.DarkGray,
        style = MaterialTheme.typography.h4,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}