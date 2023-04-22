package com.example.droidsoftthird.composable.group.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.example.droidsoftthird.model.domain_model.ApiGroup

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PagingGroupList(groups: LazyPagingItems<ApiGroup>, navigate: (String) -> Unit, ) {
    var refreshing by remember { mutableStateOf(false) }
    fun refresh () {groups.refresh()}

    val state = rememberPullRefreshState(refreshing, ::refresh)
    Box(modifier = Modifier.pullRefresh(state)) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            items(groups) {
                it?.let { group -> GroupListItem(group, navigate) }
            }
        }
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}
