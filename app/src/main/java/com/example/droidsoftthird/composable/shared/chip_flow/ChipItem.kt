package com.example.droidsoftthird.composable.shared.chip_flow

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChipItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.secondary
    }

    Chip(
        modifier = Modifier.padding(start = 4.dp, end = 4.dp),
        colors = ChipDefaults.chipColors(
            backgroundColor = backgroundColor,
            contentColor = MaterialTheme.colors.onSurface
        ),
        onClick = onClick
    ) {
        Text(label)
    }
}