package com.example.droidsoftthird.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.model.domain_model.Category

@OptIn(ExperimentalMaterial3Api::class) //TODO 実験的なAPIなので気をつける。
@Composable
fun ChipGroup(categories: Array<Category>, onClickChip: (Category) -> Unit) {
    Row {
        categories.forEach { chip ->
            AssistChip(
                label = { Text(chip.name) },
                onClick = { onClickChip(chip) },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
