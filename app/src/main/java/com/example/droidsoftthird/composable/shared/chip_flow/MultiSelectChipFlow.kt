package com.example.droidsoftthird.composable.shared.chip_flow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.composable.IconLabelItem

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun <T> MultiSelectChipFlow(
    title: String,
    icon: ImageVector,
    items: List<T>,
    selectedItems: Set<T>,
    stringProvider: @Composable (T) -> String,
    onSelected: (Set<T>) -> Unit
) {
    val rememberSelectedItems = remember(selectedItems) { mutableStateOf(selectedItems) }
    IconLabelItem(
        title = title,
        icon = icon
    )
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    ) {
        items.forEach { item ->
            val isSelected = rememberSelectedItems.value.contains(item)
            ChipItem(
                label = stringProvider(item),
                isSelected = isSelected,
                onClick = {
                    rememberSelectedItems.value = rememberSelectedItems.value.toMutableSet().apply {
                        if (isSelected) {
                            remove(item)
                        } else {
                            add(item)
                        }
                    }
                    onSelected(rememberSelectedItems.value)
                }
            )
        }
    }
    Spacer(modifier = Modifier.padding(8.dp))
}