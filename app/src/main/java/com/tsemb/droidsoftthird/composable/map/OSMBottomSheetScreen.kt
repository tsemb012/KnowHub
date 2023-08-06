package com.tsemb.droidsoftthird.composable.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.tsemb.droidsoftthird.GroupLocationsFragment
import com.tsemb.droidsoftthird.GroupLocationsViewModel
import com.example.droidsoftthird.R
import com.tsemb.droidsoftthird.composable.group.content.CommonAddButton
import com.tsemb.droidsoftthird.composable.group.content.PagingGroupList
import com.tsemb.droidsoftthird.model.domain_model.AreaCategory
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
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),  // 丸い角を作成
        sheetContent = {
            Box(Modifier.heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.7f)) {
                val lazyPagingGroups = (uiModel.value?.groupsBySelectedArea ?: emptyFlow()).collectAsLazyPagingItems()
                PagingGroupList(lazyPagingGroups, navigateToDetail, true)
            }
        },
    ) {
        Box {
            OSMContent(
                uiModel,
                fragment,
                bottomSheetState,
                scope
            ) { code: Int, category: AreaCategory -> viewModel.getGroupsByArea(code, category) }

            CommonAddButton(
                stringResource(id = R.string.group_add),
                navigate = navigateToGroupAdd,
                modifier = Modifier
                    .padding(bottom = 32.dp, end = 16.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}
