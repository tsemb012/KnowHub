package com.tsemb.droidsoftthird.composable.group.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tsemb.droidsoftthird.R
import com.tsemb.droidsoftthird.RecommendGroupsViewModel
import com.tsemb.droidsoftthird.composable.group.content.CommonAddButton
import com.tsemb.droidsoftthird.composable.group.content.PagingGroupList
import com.tsemb.droidsoftthird.composable.group.content.bottomdialog.AnimatedFilterConditionDialog
import com.tsemb.droidsoftthird.model.domain_model.ApiGroup

enum class FilterContentDestination { HOME, PREFECTURE, CITY }

@Composable
fun RecommendGroupsScreen(
    viewModel: RecommendGroupsViewModel,
    navigateToGroupDetail: (String) -> Unit,
    navigateToGroupAdd: () -> Unit,
) {

    val lazyPagingGroups = viewModel.groupsFlow.collectAsLazyPagingItems()
    val filterCondition = viewModel.groupFilterCondition
    val showDialog = remember { mutableStateOf(false) }
    val areaMap = viewModel.prefectureList to viewModel.cityList
    val onConfirm: (ApiGroup.FilterCondition?) -> Unit = {
            condition -> viewModel.updateFilterCondition(condition ?: ApiGroup.FilterCondition())
    }

    DisplayGroupListWithFab(showDialog, lazyPagingGroups, navigateToGroupDetail, navigateToGroupAdd)
    AnimatedFilterConditionDialog(showDialog, areaMap, filterCondition, onConfirm)
}

@Composable
fun DisplayGroupListWithFab(
    showDialog: MutableState<Boolean>,
    lazyPagingGroups: LazyPagingItems<ApiGroup>,
    navigateToGroupDetail: (String) -> Unit,
    navigateToGroupAdd: () -> Unit
) {
    Box {
        PagingGroupList(lazyPagingGroups, navigateToGroupDetail)
        FloatingActionButtons(showDialog, navigateToGroupAdd)
    }
}

@Composable
private fun BoxScope.FloatingActionButtons(
    showDialog: MutableState<Boolean>,
    navigateToGroupAdd: () -> Unit
) {
    Column (modifier = Modifier
        .padding(bottom = 32.dp, end = 16.dp)
        .align(Alignment.BottomEnd)
    ) {
        FloatingActionButton(
            onClick = { showDialog.value = true },
            backgroundColor = colorResource(id = R.color.primary_accent_yellow),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Filled.FilterList, contentDescription = "Filter", tint = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        CommonAddButton(stringResource(id = R.string.group_add),  navigateToGroupAdd, Modifier.align(Alignment.End))
    }
}
