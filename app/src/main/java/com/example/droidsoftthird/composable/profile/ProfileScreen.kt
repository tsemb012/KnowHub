package com.example.droidsoftthird.composable.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.droidsoftthird.ProfileViewModel
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.shared.SharedTextLines
import com.example.droidsoftthird.composable.shared.TextSize
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.ItemEvent

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    toProfileEdit: () -> Unit,
    onLogOut: () -> Unit,
) {
    val userDetail = viewModel.userDetail
    val context = LocalContext.current
    val comment = userDetail.value.comment
    val groups = userDetail.value.groups
    val events = userDetail.value.events

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Edit Profile") },
                icon = { Icon(Icons.Filled.AddCircle, contentDescription = null) },
                onClick = { toProfileEdit() }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { innerPadding ->
            Log.d("ProfileScreen", "userDetail.value.userImage: ${innerPadding}")

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    Box (
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(viewModel.downloadUrl1.value)
                                .build(),
                            contentDescription = "User Image",
                            modifier = Modifier
                                .padding(16.dp)
                                .size(150.dp)
                                .clip(RoundedCornerShape(100.dp)),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    SharedTextLines(
                        title =  userDetail.value.userName,
                        text = userDetail.value.comment,
                        titleTextSize = TextSize.LARGE,
                        descriptionTextSize = TextSize.MED_LARGE,
                    )
                    ProfileSpacerAndDivider()
                    SharedTextLines(
                        title = stringResource(id = R.string.gender),
                        text = userDetail.value.getJapanese(userDetail.value.gender),
                        hasSpace = true
                    )
                    ProfileSpacerAndDivider()
                    SharedTextLines(
                        title = stringResource(id = R.string.birthday),
                        text = userDetail.value.formattedBirthday,
                        hasSpace = true
                    )
                    ProfileSpacerAndDivider()
                    SharedTextLines(
                        title = stringResource(id = R.string.residential_area),
                        text = userDetail.value.residentialArea,
                        hasSpace = true
                    )
                    ProfileSpacerAndDivider()
                    Text("所属グループ", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.h6, modifier = Modifier)



                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        groups.forEach { group ->
                            GroupCard(group = group)
                        }
                    }

                    ProfileSpacerAndDivider()
                    Text("参加予定のイベント", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.h6, modifier = Modifier)

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        events.forEach { event ->
                            EventCard(event = event)
                        }
                    }
                }
                item {
                    SignOutButton(onLogOut = onLogOut)
                }
            }
        }
    )
}

@Composable
fun SignOutButton (onLogOut: () -> Unit) {
    Button(
        onClick = { onLogOut() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
    ) {
        Text(text = "Sign Out")
    }
}

@Composable
fun ProfileInfoItem(title: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold)
            Text(text = value)
        }
    }
}

@Composable
fun ProfileSpacerAndDivider() {
    Spacer(modifier = Modifier.height(16.dp))
    androidx.compose.material.Divider()
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun Divider() {
    Divider(
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun GroupCard(group: ApiGroup) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = group.groupName, fontWeight = FontWeight.Bold)
            Text(text = group.groupIntroduction)
            Text(text = stringResource(group.groupType.displayNameId))
        }
    }
}

@Composable
fun EventCard(event: ItemEvent) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.name, fontWeight = FontWeight.Bold)
            Text(text = event.groupName ?: "")
            Text(text = event.placeName ?: "")
            Text(text = "${event.period.first.toLocalDate()}")
            Text(text = "${event.period.first.hour} - ${event.period.second.hour}")
        }
    }
}