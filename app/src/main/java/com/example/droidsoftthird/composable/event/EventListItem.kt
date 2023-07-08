package com.example.droidsoftthird.composable.event

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Text(text = event.toString())
}
