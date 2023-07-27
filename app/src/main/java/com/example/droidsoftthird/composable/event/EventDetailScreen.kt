package com.example.droidsoftthird.composable.event

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.map.MapWithMarker
import com.example.droidsoftthird.model.domain_model.EventDetail
import org.jitsi.meet.sdk.ParticipantInfo

@Composable
fun EventDetailScreen(
    event: MutableState<EventDetail?>,
    startVideoChat: () -> Unit,
    deleteEvent: () -> Unit,
    onBack: () -> Unit,
) {


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

                if (event.value?.isOnline == true) {
                    Text("オンラインイベント", style = MaterialTheme.typography.h6)
                } else {
                    MapWithMarker(event,
                        Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    )
                }

                /*event.value?.let { eventDetail ->

                    if (eventDetail.hostId == eventViewModel.userId) {
                        Button(
                            onClick = { deleteEvent() },
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) { Text("イベントを削除") }
                    }
                    if (eventDetail.place == null) {
                        Button(
                            enabled = eventViewModel.isVideoChatAvailable,
                            onClick = { startVideoChat() },
                            content = {
                                Text(
                                    if (eventViewModel.isVideoChatAvailable) "ビデオチャット"
                                    else if (eventViewModel.isFinished) "ビデオチャットは終了しました"
                                    else if (eventViewModel.isNotStarted) "開始時間までお待ちください"
                                    else "ビデオチャットは準備中です"
                                    )
                            }
                        )
                    }


                    if (eventDetail.registeredUserIds.contains(eventViewModel.userId)) {
                            Button(
                                onClick = { eventViewModel.leaveEvent() },
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text("イベントから抜ける")
                            }
                        } else {
                            Button(
                                onClick = { eventViewModel.joinEvent() },
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text("イベントに参加")
                            }
                        }

                    ListItem(title = "イベント名", content = eventDetail.name)
                    ListItem(title = "コメント", content = eventDetail.comment)
                    ListItem(title = "日付", content = eventDetail.startDateTime.toString())
                    ListItem(title = "開始時間", content = eventDetail.startDateTime.toString())
                    ListItem(title = "終了時間", content = eventDetail.endDateTime.toString())

                    eventDetail.place?.let { place ->
                        ListItem(title = "場所名", content = place.name)
                        ListItem(title = "住所", content = place.formattedAddress ?: "不明")
                    }

                    ListItem(title = "グループ名", content = eventDetail.groupName)
                    ListItem(title = "登録ユーザー数", content = eventDetail.registeredUserIds.size.toString())
                }*/
                Log.d("EventDetailScreen", "$it")
            }
        }
    )
}

@Composable
fun ParticipantInfo(participantInfo: ParticipantInfo, onIconClick: () -> Unit) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = participantInfo.displayName,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_access_time_24),
                contentDescription = "Description for accessibility",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onIconClick)
            )
            Text(text = "参加人数")
        }
        HorizontalUserIcons(userImages = participantInfo.userImages)
    }
}

@Composable
fun HorizontalUserIcons(userImages: List<String>) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        userImages.forEach { imageUrl ->
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .padding(4.dp)
            )
        }
    }
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
