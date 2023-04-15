package com.example.droidsoftthird.composable.group

import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.droidsoftthird.GroupList
import com.example.droidsoftthird.RecommendGroupsViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun RecommendGroupsScreen(viewModel: RecommendGroupsViewModel, navigateToGroupDetail: (String) -> Unit) {

    val error = viewModel.errorLiveData

    val lazyPagingGroups = viewModel.groupsFlow.collectAsLazyPagingItems()
    GroupList(lazyPagingGroups, navigateToGroupDetail)

}