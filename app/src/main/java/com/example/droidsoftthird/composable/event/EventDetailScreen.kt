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
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.map.MapWithMarker
import com.example.droidsoftthird.composable.shared.CommonLinearProgressIndicator
import com.example.droidsoftthird.composable.shared.DescriptionItem
import com.example.droidsoftthird.composable.shared.SharedDescriptions
import com.example.droidsoftthird.composable.shared.SharedTextField
import com.example.droidsoftthird.composable.shared.SharedTextLines
import com.example.droidsoftthird.composable.shared.TextSize
import com.example.droidsoftthird.model.domain_model.EventDetail
import com.example.droidsoftthird.model.domain_model.SimpleUser

@Composable
fun EventDetailScreen(
    event: MutableState<EventDetail?>,
    isLoading: MutableState<Boolean>,
    startVideoChat: () -> Unit,
    deleteEvent: () -> Unit,
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.background)
                            .padding(16.dp)
                    ) {

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
                            ParticipantInfo2(event.value, {navigateToGroupDetail () }) {}
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
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()



                        }
                        Log.d("EventDetailScreen", "$it")
                    }
                }
            )
        }
    }
}


@Composable
fun ParticipantInfo2(eventDetail: EventDetail, onLauncherClick: () -> Unit ,onIconClick: () -> Unit) {
    val participants = eventDetail.eventRegisteredMembers
    val totalMembers = eventDetail.groupMembers
    val groupName = eventDetail.groupName
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
            Image(
                painter = rememberImagePainter(user.userImage),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
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
