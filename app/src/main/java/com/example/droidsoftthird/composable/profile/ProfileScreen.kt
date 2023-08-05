package com.example.droidsoftthird.composable.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.droidsoftthird.ProfileViewModel
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.shared.DescriptionItem
import com.example.droidsoftthird.composable.shared.SharedConfirmButton
import com.example.droidsoftthird.composable.shared.SharedDescriptions
import com.example.droidsoftthird.composable.shared.SharedTextLines
import com.example.droidsoftthird.composable.shared.TextSize
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.EventItem
import com.example.droidsoftthird.utils.webview.openUrl

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    toProfileEdit: () -> Unit,
    toGroupDetail: (String) -> Unit,
    toEventDetail: (String) -> Unit,
    toLicense: () -> Unit,
    onLogOut: () -> Unit,
    onWithdraw: () -> Unit
) {
    val userDetail = viewModel.userDetail
    val context = LocalContext.current
    val comment = userDetail.value.comment
    val groups = userDetail.value.groups
    val events = userDetail.value.events

    val showSignOutDialog = remember { mutableStateOf(false) }
    val showWithdrawDialog = remember { mutableStateOf(false) }


    if (showSignOutDialog.value) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog.value = false },
            title = { Text("本当にログアウトしますか？") },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.primary_dark),
                        contentColor = Color.White
                    ),
                    onClick = {
                        onLogOut()
                        showSignOutDialog.value = false
                    }
                ) {
                    Text("はい")
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Gray,
                        contentColor = Color.White
                    ),
                    onClick = { showSignOutDialog.value = false }
                ) {
                    Text("いいえ")
                }
            }
        )
    }

    if (showWithdrawDialog.value) {
        AlertDialog(
            onDismissRequest = { showWithdrawDialog.value = false },
            title = { Text("本当に退会しますか？") },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.primary_dark),
                        contentColor = Color.White
                    ),
                    onClick = {
                        onWithdraw()
                        showWithdrawDialog.value = false
                    }
                ) {
                    Text("はい")
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Gray,
                        contentColor = Color.White
                    ),
                    onClick = { showWithdrawDialog.value = false }
                ) {
                    Text("いいえ")
                }
            }
        )
    }

    Scaffold(
        content = { innerPadding ->
            Log.d("ProfileScreen", "userDetail.value.userImage: ${innerPadding}")

            Box {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box {
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
                                Button(
                                    onClick = { toProfileEdit() },
                                    modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 8.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        colorResource(id = R.color.primary_dark)
                                    ),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = colorResource(id = R.color.base_100),
                                        contentColor = colorResource(id = R.color.primary_dark),
                                    ),
                                    elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
                                ) {
                                    Text("編集")
                                }
                            }
                        }
                        SharedTextLines(
                            title = userDetail.value.userName,
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
                        Text(
                            "所属グループ",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                        ) {
                            groups.forEach { group ->
                                GroupCard(group = group, toGroupDetail)
                            }
                        }

                        ProfileSpacerAndDivider()
                        Text(
                            "参加予定のイベント",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                        ) {
                            events.forEach { event ->
                                EventCard(event = event, toEventDetail)
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(36.dp))
                        SharedConfirmButton(
                            text = "ログアウト",
                            onConfirm = { showSignOutDialog.value = true },
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.width(300.dp)
                        )
                    }



                    item {
                        val context = LocalContext.current
                        val termsOfUseUrl = "https://sites.google.com/view/workandchillapp-termofuse/%E3%83%9B%E3%83%BC%E3%83%A0"
                        val privacyPolicyUrl = "https://sites.google.com/view/workandchillapp-privacypolicy/%E3%83%9B%E3%83%BC%E3%83%A0"
                        Spacer(modifier = Modifier.height(36.dp))
                        AppendixButton("利用規約") { openUrl(context, termsOfUseUrl) }
                        AppendixButton("プライバシーポリシー") { openUrl(context, privacyPolicyUrl) }
                        AppendixButton("ライセンス") { toLicense() }
                        AppendixButton("退会") { showWithdrawDialog.value = true }
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "開発者 問い合わせ先\nE-mail: workandchillapp@gmail.com",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center,
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Divider()
                    }
                    item { Spacer(modifier = Modifier.height(120.dp)) }
                }
            }
        }
    )


}


@Composable
fun AppendixButton(text: String, textSize: TextStyle = MaterialTheme.typography.h5, onClick: () -> Unit) {
    Divider()
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.elevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.Gray,
                style = textSize,
            )
        }
    }
}


@Composable
fun ProfileSpacerAndDivider() {
    Spacer(modifier = Modifier.height(16.dp))
    Divider()
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun GroupCard(group: ApiGroup, toGroupDetail: (String) -> Unit, ) {
    
    val title = group.groupName
    val itemList = listOf(
        DescriptionItem(Icons.Filled.Group, group.availability, 1),
        DescriptionItem(Icons.Filled.LocationOn, group.location, 1),
        DescriptionItem(Icons.Filled.Comment, group.groupIntroduction, 2),
    )
    SharedBoxCard(title, itemList) { group.groupId?.let { toGroupDetail(it) } }
}

@Composable
fun EventCard(event: EventItem, toEventDetail: (String) -> Unit,) {
    val title = event.name
    val itemList = listOf(
        DescriptionItem(Icons.Filled.CalendarToday, event.formattedDate, 1),
        DescriptionItem(Icons.Filled.AvTimer, event.formattedPeriod, 1),
        DescriptionItem(Icons.Filled.LocationOn, event.placeName ?: "", 1),
        DescriptionItem(Icons.Filled.Group, event.groupName ?: "", 1),
    )
    SharedBoxCard(title, itemList) { toEventDetail(event.eventId) }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun SharedBoxCard(
    title: String,
    itemList: List<DescriptionItem>,
    navigateTo: () -> Unit?,
    ) {
    Card(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 0.dp)
            .width(220.dp)
            .height(175.dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        elevation = 4.dp,
        onClick = { navigateTo() }
    ) {

        Box(modifier = Modifier.padding(8.dp)) {
            SharedDescriptions(
                title = title,
                itemList = itemList,
            )
        }
    }
}


