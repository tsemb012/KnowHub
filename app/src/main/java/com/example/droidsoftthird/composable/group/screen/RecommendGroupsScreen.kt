package com.example.droidsoftthird.composable.group.screen

import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.droidsoftthird.RecommendGroupsViewModel
import com.example.droidsoftthird.composable.group.content.PagingGroupList

@Composable
fun RecommendGroupsScreen(viewModel: RecommendGroupsViewModel, navigateToGroupDetail: (String) -> Unit) {

    val error = viewModel.errorLiveData

    val lazyPagingGroups = viewModel.groupsFlow.collectAsLazyPagingItems()
    PagingGroupList(lazyPagingGroups, navigateToGroupDetail)

}