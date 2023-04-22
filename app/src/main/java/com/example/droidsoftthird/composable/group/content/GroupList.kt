package com.example.droidsoftthird.composable.group.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.model.domain_model.ApiGroup

@Composable
fun GroupList(groups: List<ApiGroup>, navigate : (String) -> Unit) {
    Box() {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            items(groups.size) { GroupListItem(groups[it], navigate) }
        }
    }
}
