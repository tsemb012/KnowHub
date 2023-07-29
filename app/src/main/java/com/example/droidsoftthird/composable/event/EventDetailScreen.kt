package com.example.droidsoftthird.composable.event

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.map.MapWithMarker
import com.example.droidsoftthird.composable.shared.CommonLinearProgressIndicator
import com.example.droidsoftthird.composable.shared.DescriptionItem
import com.example.droidsoftthird.composable.shared.SharedConfirmButton
import com.example.droidsoftthird.composable.shared.SharedDescriptions
import com.example.droidsoftthird.composable.shared.SharedTextLines
import com.example.droidsoftthird.composable.shared.TextSize
import com.example.droidsoftthird.model.domain_model.EventDetail
import com.example.droidsoftthird.model.domain_model.SimpleUser

@Composable
fun EventDetailScreen(
    event: MutableState<EventDetail?>,
    isLoading: MutableState<Boolean>,
    isJoined: MutableState<Boolean>,
    startVideoChat: () -> Unit,
    deleteEvent: () -> Unit,
    joinEvent: () -> Unit,
    leaveEvent: () -> Unit,
    navigateToGroupDetail: () -> Unit,
    onBack: () -> Unit,
) {
    Box (Modifier.background(color = colorResource(id = R.color.base_100))) {
        if (isLoading.value) CommonLinearProgressIndicator ()
        else {
            val event1 = event.value!!
            val event = mutableStateOf(event1)
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
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.background)
                                .padding(vertical = 16.dp, horizontal = 20.dp)
                        ) {

                            item {
                                if (event.value.isOnline == true) {
                                    Text("オンラインイベント", style = MaterialTheme.typography.h6)
                                } else {
                                    MapWithMarker(
                                        event,
                                        Modifier
                                            .height(200.dp)
                                            .fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    SharedTextLines(
                                        title = event.value.name,
                                        text = event.value.comment,
                                        titleTextSize = TextSize.LARGE,
                                        descriptionTextSize = TextSize.MEDIUM,
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Divider()
                                    Spacer(modifier = Modifier.height(16.dp))
                                    ParticipantInfo2(event.value, { navigateToGroupDetail() }) {}
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Divider()
                                    Spacer(modifier = Modifier.height(16.dp))
                                    SharedDescriptions(
                                        title = "日時",
                                        itemList = listOf(
                                            DescriptionItem(text = event.value.formattedDate),
                                            DescriptionItem(text = event.value.formattedPeriod),
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Divider()
                                    Spacer(modifier = Modifier.height(16.dp))
                                    val descriptionItems = listOf(
                                        DescriptionItem(
                                            Icons.Filled.LocationOn,
                                            (event.value.place?.name + "\n" + event.value.place?.formattedAddress),
                                            3
                                        ),
                                        DescriptionItem(
                                            Icons.Filled.Phone,
                                            event.value.place?.tel ?: "",
                                            1
                                        ),
                                        DescriptionItem(
                                            Icons.Filled.Language,
                                            event.value.place?.url ?: "",
                                            2,
                                            true
                                        ),
                                    )
                                    SharedDescriptions(
                                        title = "場所",
                                        itemList = descriptionItems
                                    )


                                }
                                Log.d("EventDetailScreen", "$it")
                            }
                            item { Spacer(modifier = Modifier.height(100.dp)) }
                        }
                }
            )
        }
        Column(modifier = Modifier.wrapContentHeight().fillMaxWidth().align(Alignment.BottomCenter).background(color = colorResource(id = R.color.base_100))) {
            Divider()
            ConfirmEventButton(!isLoading.value, isJoined.value, joinEvent, leaveEvent)
        }
    }
}

@Composable
fun ConfirmEventButton(isEditable: Boolean, isJoined: Boolean, joinEvent: () -> Unit, leaveEvent: () -> Unit) {
        if (isJoined) SharedConfirmButton("イベントを抜ける", isEditable, leaveEvent)
        else SharedConfirmButton("イベントに参加", isEditable, joinEvent)
    }


@Composable
fun ParticipantInfo2(eventDetail: EventDetail, onLauncherClick: () -> Unit ,onIconClick: () -> Unit) {
    val participants = eventDetail.eventRegisteredMembers
    val totalMembers = eventDetail.groupMembers
    val groupName = eventDetail.groupName
    //participants.first().userName
    Column {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = groupName,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.baseline_launch_24),
                contentDescription = "Description for accessibility",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onLauncherClick),
                tint = colorResource(id = R.color.primary_dark),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "参加人数 ${eventDetail.registrationRatio}", style = MaterialTheme.typography.body1)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Spacer(modifier = Modifier.width(16.dp))
            HorizontalUserIcons(users = participants)
        }
    }
}

@Composable
fun HorizontalUserIcons(users: List<SimpleUser>) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        users.forEach { user ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberImagePainter(user.userImage),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = user.userName,
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
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
