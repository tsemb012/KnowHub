package com.example.droidsoftthird.composable.group.screen

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.droidsoftthird.R
import com.example.droidsoftthird.RecommendGroupsViewModel
import com.example.droidsoftthird.composable.group.content.PagingGroupList
import com.example.droidsoftthird.model.domain_model.*
import com.example.droidsoftthird.model.domain_model.ApiGroup.FilterCondition.Companion.getPrefectureCode
import com.example.droidsoftthird.repository.csvloader.CityCsvLoader
import com.example.droidsoftthird.repository.csvloader.PrefectureCsvLoader

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
        FloatingActionButton(
            onClick = { navigateToGroupAdd() },
            backgroundColor = colorResource(id = R.color.primary_dark),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.align(Alignment.End)
        ) {
            Row (
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
                Text(text = stringResource(id = R.string.group_add), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}