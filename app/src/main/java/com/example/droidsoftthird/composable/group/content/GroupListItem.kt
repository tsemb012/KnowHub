package com.example.droidsoftthird.composable.group.content

import android.graphics.drawable.Icon
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.FacilityEnvironment
import com.example.droidsoftthird.model.domain_model.FrequencyBasis
import com.example.droidsoftthird.model.domain_model.GroupType
import com.example.droidsoftthird.model.domain_model.Style

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupListItem(group: ApiGroup, navigateToDetail: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 8.dp,
        onClick = { group.groupId?.let { navigateToDetail(it) } }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(model = group.storageRef,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .align(Alignment.CenterVertically)
                    .border(1.dp, Color.Black.copy(alpha = 0.1F), RoundedCornerShape(24.dp)),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,

            )
            Spacer(modifier = Modifier.width(16.dp))
            //TODO 下記をまとめて再利用できるように
            Column {
                /*Text(text = group.groupName, style = MaterialTheme.typography.h6, color = Color.Gray)

                val itemList = listOf(
                    Icons.Filled.Group to group.groupIntroduction,
                    "facilityEnvironment" to group.facilityEnvironment,
                    "style" to group.style,
                    "basis" to group.basis,
                    "frequency" to group.frequency,
                    "minAge" to group.minAge,
                    "maxAge" to group.maxAge,
                    "maxNumberPerson" to group.maxNumberPerson,
                    "isChecked" to group.isChecked,
                )

                Column(modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp)) {

                    Text(text = group.groupIntroduction, style = MaterialTheme.typography.body1)
                    Text(
                        text = "${group.prefecture} ${group.city}",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.align(Alignment.End)
                    )
                }*/
            }
        }
    }
}

@Preview
@Composable
fun PreviewGroupListItem() {
    val group1 = ApiGroup(
        groupId = "1", hostUserId = "1", groupName = "group1", groupIntroduction = "group1",
        groupType = GroupType.INDIVIDUAL_TASK, prefecture = "prefecture", city = "city", isOnline = false,
        facilityEnvironment = FacilityEnvironment.OTHER_FACILITY_ENVIRONMENT, basis = FrequencyBasis.MONTHLY, style = Style.FOCUS,
        frequency = 1, minAge = 10, maxAge = 40, maxNumberPerson = 10, isChecked = false, storageRef = "https://images.pexels.com/photos/9954174/pexels-photo-9954174.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"

    )
    GroupListItem(group = group1, navigateToDetail = {})
}
