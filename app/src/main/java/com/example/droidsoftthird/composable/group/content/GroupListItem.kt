package com.example.droidsoftthird.composable.group.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.droidsoftthird.model.domain_model.ApiGroup

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupListItem(group: ApiGroup, navigateToDetail: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 8.dp,
        onClick = { group.groupId?.let { navigateToDetail(it) } }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AsyncImage(model = group.storageRef,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = group.groupName, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = group.groupIntroduction, style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${group.prefecture} ${group.city}",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
