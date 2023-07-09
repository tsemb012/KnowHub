package com.example.droidsoftthird.composable.event

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.VideoChat
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.shared.ItemDescription
import com.example.droidsoftthird.composable.shared.FundamentalCard
import com.example.droidsoftthird.composable.shared.ItemStatus
import com.example.droidsoftthird.model.domain_model.EventStatus
import com.example.droidsoftthird.model.domain_model.ItemEvent
import com.example.droidsoftthird.utils.converter.formatTimePeriod

@Composable
fun EventListItem(event: ItemEvent, navigateToDetail: (String) -> Unit) {
    FundamentalCard(
        onClick = { navigateToDetail(event.eventId) },
        content = { EventCardContent(event) }
    )
}

@Composable
fun EventCardContent(event: ItemEvent) {

    val descriptionList = listOfNotNull(
        Triple(Icons.Filled.Group, event.groupName ?: "", 1),
        Triple(Icons.Filled.AvTimer, formatTimePeriod(event.period.first, event.period.second), 1),
        if (!event.isOnline) Triple(Icons.Filled.LocationOn, event.placeName ?: "", 1) else null,
    )
    //TODO 日付の文字列をうまく表現する。Creat画面のロジックをアレンジするようにする。

    val statusColor = when {
        event.status == EventStatus.BEFORE_REGISTRATION -> R.color.gph_dark_red to "参加受付中"
        event.status == EventStatus.AFTER_REGISTRATION_BEFORE_EVENT -> R.color.blue to "参加予定"
        event.status == EventStatus.AFTER_REGISTRATION_DURING_EVENT -> R.color.gray to "開催中"
        event.status == EventStatus.AFTER_EVENT -> R.color.gray_light to "イベント終了"
        else -> R.color.gph_dark_red to "エラー"
    }

    val statusList = listOfNotNull(
        Triple(Icons.Filled.Group, statusColor.first, statusColor.second),
        if (event.isOnline) Triple(Icons.Filled.VideoChat, R.color.primary_dark, "オンライン") else null,
    )

    Column {
        ItemDescription(event.name, descriptionList)
        ItemStatus(statusList)
        Text(text = "${event.registrationRatio} 人" )
    }
}
