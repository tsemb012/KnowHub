package com.example.droidsoftthird.composable.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.droidsoftthird.GroupLocationsFragment
import com.example.droidsoftthird.GroupLocationsViewModel
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.group.content.GroupAddButton
import com.example.droidsoftthird.composable.group.content.PagingGroupList
import com.example.droidsoftthird.model.domain_model.AreaCategory
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OSMBottomSheetScreen(
    viewModel: GroupLocationsViewModel,
    fragment: GroupLocationsFragment,
    navigateToDetail: (String) -> Unit,
    navigateToGroupAdd: () -> Unit
) {
    val uiModel = viewModel.uiModel.observeAsState()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            val lazyPagingGroups = (uiModel.value?.groupsBySelectedArea ?: emptyFlow()).collectAsLazyPagingItems()
            PagingGroupList(lazyPagingGroups, navigateToDetail,)
        }
    ) {
        Box {
            OSMContent(
                uiModel,
                fragment,
                bottomSheetState,
                scope
            ) { code: Int, category: AreaCategory -> viewModel.getGroupsByArea(code, category) }

            GroupAddButton(navigateToGroupAdd = navigateToGroupAdd, modifier = Modifier
                .padding(bottom = 32.dp, end = 16.dp)
                .align(Alignment.BottomEnd)
            )
        }
    }
}
