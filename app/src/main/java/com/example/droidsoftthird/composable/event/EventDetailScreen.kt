package com.example.droidsoftthird.composable.event

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.map.MapWithMarker
import com.example.droidsoftthird.composable.shared.CommonLinearProgressIndicator
import com.example.droidsoftthird.model.domain_model.EventDetail
import com.example.droidsoftthird.model.domain_model.SimpleUser

@Composable
fun EventDetailScreen(
    event: MutableState<EventDetail?>,
    isLoading: MutableState<Boolean>,
    startVideoChat: () -> Unit,
    deleteEvent: () -> Unit,
    onBack: () -> Unit,
) {
    Box {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "イベント詳細", color = Color.Black) },
                    backgroundColor = Color.Transparent,
                    contentColor = Color.Black,
                    elevation = 0.dp,
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "戻る",
                                tint = Color.Black
                            )
                        }
                    }
                )
            },
            content = {
                Log.d("EventDetailScreen", "it: $it")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background)
                        .padding(16.dp)
                ) {

                    if (event.value?.isOnline == true) {
                        Text("オンラインイベント", style = MaterialTheme.typography.h6)
                    } else {
                        MapWithMarker(
                            event,
                            Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                        )

                        if (event.value != null) {
                            ParticipantInfo2(event.value!!) {
                            }
                        }

                    }
                    Log.d("EventDetailScreen", "$it")
                }
            }
        )
        if (isLoading.value) CommonLinearProgressIndicator()
    }
}


@Composable
fun ParticipantInfo2(eventDetail: EventDetail, onIconClick: () -> Unit) {
    val participants = eventDetail.eventRegisteredMembers
    val totalMembers = eventDetail.groupMembers
    val groupName = eventDetail.groupName
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = groupName,
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
        HorizontalUserIcons(users = participants)
    }
}

@Composable
fun HorizontalUserIcons(users: List<SimpleUser>) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        users.forEach { user ->
            Image(
                painter = rememberImagePainter(user.userImage),
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
