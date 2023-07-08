package com.example.droidsoftthird.composable.event

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import com.example.droidsoftthird.composable.ItemDescription
import com.example.droidsoftthird.composable.shared.FundamentalCard
import com.example.droidsoftthird.model.domain_model.ItemEvent

@Composable
fun EventListItem(event: ItemEvent, navigateToDetail: (String) -> Unit) {
    FundamentalCard(
        onClick = { navigateToDetail(event.eventId) },
        content = { EventCardContent(event) }
    )
}

@Composable
fun EventCardContent(event: ItemEvent) {
    val itemList = listOf(
        Triple(Icons.Filled.Group, event.groupName ?: "", 1),
        Triple(Icons.Filled.AvTimer, event.period.toString(), 1),
        Triple(Icons.Filled.LocationOn, event.placeName ?: "", 1),
        )
    ItemDescription(event.name, itemList)
}
