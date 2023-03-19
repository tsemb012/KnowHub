package com.example.droidsoftthird.composable.event

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.ScheduleDetailViewModel
import com.example.droidsoftthird.composable.map.MapWithMarker

@Composable
fun EventDetailScreen(
        eventViewModel: ScheduleDetailViewModel,
        onBack: () -> Unit
) {
    val event = eventViewModel.eventDetail

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "イベント詳細") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
                    .padding(16.dp)
            ) {
                event.value?.let { eventDetail ->
                    MapWithMarker(eventViewModel, Modifier.height(200.dp).fillMaxWidth())

                    Button(
                        onClick = { eventViewModel.joinEvent() },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text("イベントに参加")
                    }

                    ListItem(title = "イベント名", content = eventDetail.name)
                    ListItem(title = "コメント", content = eventDetail.comment)
                    ListItem(title = "日付", content = eventDetail.date.toString())
                    ListItem(title = "開始時間", content = eventDetail.startTime.toString())
                    ListItem(title = "終了時間", content = eventDetail.endTime.toString())

                    eventDetail.place?.let { place ->
                        ListItem(title = "場所名", content = place.name)
                        ListItem(title = "住所", content = place.formattedAddress ?: "不明")
                    }

                    ListItem(title = "グループ名", content = eventDetail.groupName)
                    ListItem(title = "登録ユーザー数", content = eventDetail.registeredUserIds.size.toString())
                }
                Log.d("EventDetailScreen", "$it")
            }
        }
    )
}

@Composable
fun ListItem(title: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = "$title: ", style = MaterialTheme.typography.body1, modifier = Modifier.weight(1f))
        Text(text = content, style = MaterialTheme.typography.body1, modifier = Modifier.weight(2f))
    }
}
