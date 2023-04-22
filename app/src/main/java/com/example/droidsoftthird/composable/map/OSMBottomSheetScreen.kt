package com.example.droidsoftthird.composable.map

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.droidsoftthird.GroupLocationsFragment
import com.example.droidsoftthird.GroupLocationsViewModel
import com.example.droidsoftthird.composable.group.content.PagingGroupList
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OSMBottomSheetScreen(
    viewModel: GroupLocationsViewModel,
    fragment: GroupLocationsFragment,
    navigateToDetail: (String) -> Unit
) {
    val uiModel = viewModel.uiModel.observeAsState()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            val lazyPagingGroups = (uiModel.value?.groupsBySelectedArea ?: emptyFlow()).collectAsLazyPagingItems()
            PagingGroupList(
                lazyPagingGroups,
                navigateToDetail,
            )
        }
    ) {
        OSMContent(
            uiModel,
            fragment,
            bottomSheetState,
            scope
        ) { code:Int, category:String ->  viewModel.getGroupsByArea(code, category) }
    }
}
