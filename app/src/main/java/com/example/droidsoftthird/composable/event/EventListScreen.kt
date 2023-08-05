package com.example.droidsoftthird.composable.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.ScheduleListViewModel
import com.example.droidsoftthird.model.domain_model.EventItem


@Composable
fun EventListScreen(viewModel: ScheduleListViewModel, onSelectEvent: (String) -> Unit) {

    viewModel.uiModel.observeAsState().value?.let { uiModel ->
        EventList(uiModel.groupFilteredEvents, onSelectEvent)
    }
}

@Composable
fun EventList(events: List<EventItem>, onSelectEvent: (String) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(events) { event -> EventCard(event, onSelectEvent) }
    }
}

@Composable
fun EventCard(event: EventItem, onSelectEvent: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = { onSelectEvent (event.eventId)}
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ListItem(title = "イベント名", content = event.name)
                ListItem(title = "日付", content = event.period.first.dayOfMonth.toString())
                ListItem(title = "コメント", content = event.comment)
                ListItem(title = "期間", content = event.period.toString())
                ListItem(title = "コミュニティ名", content = event.groupName ?: "なし")
                ListItem(title = "場所名", content = event.placeName ?: "なし")
            } //TODO ここにグループ参加と不参加のロジックを入れるようにする。
        }
    }
}
