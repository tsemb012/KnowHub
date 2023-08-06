package com.example.droidsoftthird.composable.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.shared.SharedDescriptions
import com.example.droidsoftthird.composable.shared.DescriptionItem
import com.example.droidsoftthird.composable.shared.FundamentalCard
import com.example.droidsoftthird.composable.shared.ItemStatus
import com.example.droidsoftthird.model.domain_model.EventStatus
import com.example.droidsoftthird.model.domain_model.EventItem
import com.example.droidsoftthird.utils.converter.formatTimePeriod
import java.time.ZonedDateTime

@Composable
fun EventListItem(event: EventItem, navigateToDetail: (String) -> Unit) {
    FundamentalCard(
        onClick = { navigateToDetail(event.eventId) },
        content = { EventCardContent(event) }
    )
}

@Composable
fun EventCardContent(event: EventItem) {

    val descriptionList: List<DescriptionItem> = listOf(
        DescriptionItem(Icons.Filled.Group, event.groupName ?: "", 1, false),
        DescriptionItem(Icons.Filled.AvTimer, formatTimePeriod(event.period.first, event.period.second), 1, false),
        DescriptionItem(Icons.Filled.LocationOn, if (!event.isOnline) event.placeName ?: "" else "-", 1, false),
    )

    //TODO 日付の文字列をうまく表現する。Creat画面のロジックをアレンジするようにする。

    val statusColor = when (event.status) {
        EventStatus.BEFORE_REGISTRATION -> event.status.getStatusColor() to event.status.getStatusText()
        EventStatus.BEFORE_REGISTRATION_DURING_EVENT -> event.status.getStatusColor() to event.status.getStatusText()
        EventStatus.AFTER_REGISTRATION_BEFORE_EVENT -> event.status.getStatusColor() to event.status.getStatusText()
        EventStatus.AFTER_REGISTRATION_BEFORE_EVENT -> event.status.getStatusColor() to event.status.getStatusText()
        EventStatus.AFTER_REGISTRATION_DURING_EVENT -> event.status.getStatusColor() to event.status.getStatusText()
        EventStatus.AFTER_EVENT -> R.color.gray to event.status.getStatusText()
    }

    val statusList = listOfNotNull(
        Triple(Icons.Filled.Circle, statusColor.first, statusColor.second),
        if (event.isOnline) Triple(Icons.Filled.Videocam, R.color.primary_dark, "オンライン") else null,
    )

    Row(modifier = Modifier
        .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        SharedDescriptions(event.name, descriptionList, modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp))
        Box(modifier = Modifier.width(100.dp).height(100.dp).padding(bottom = 16.dp)) {
            ItemStatus(statusList)
            Text(
                text = "${event.registrationRatio} 人",
                fontSize = 20.sp,
                color = colorResource(id = R.color.dark_gray),
                modifier = Modifier.align(Alignment.BottomStart).padding(start = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun EventListItemPreview() {
    val event = EventItem(
        eventId = "1",
        name = "イベント名",
        groupName = "コミュニティ名",
        comment = "コメント",
        groupId = "1",
        eventRegisteredNumber = 1,
        groupJoinedNumber = 1,
        period = Pair(
            ZonedDateTime.parse("2021-09-01T10:00:00+09:00[Asia/Tokyo]"),
            ZonedDateTime.parse("2021-09-01T12:00:00+09:00[Asia/Tokyo]")),
        placeName = "場所",
        isOnline = false,
        status = EventStatus.BEFORE_REGISTRATION,
    )
    EventListItem(event = event, navigateToDetail = {})
}
