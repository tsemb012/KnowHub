package com.example.droidsoftthird.composable.event

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.shared.ItemDescription
import com.example.droidsoftthird.composable.shared.FundamentalCard
import com.example.droidsoftthird.composable.shared.ItemStatus
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

    val descriptionList = listOf(
        Triple(Icons.Filled.Group, event.groupName ?: "", 1),
        Triple(Icons.Filled.AvTimer, event.period.toString(), 1),
        Triple(Icons.Filled.LocationOn, event.placeName ?: "", 1),
    )

    /*val statusColor = when {
        event.period
    }*/

    val statusList = listOf(
        Triple(Icons.Filled.Group, R.color.gph_dark_red, "テスト"),
        Triple(Icons.Filled.AvTimer, R.color.gray, "テスト"),
    )

    Column {
        ItemDescription(event.name, descriptionList)
        ItemStatus(statusList)
        Text(text = "人数")
    }
}
