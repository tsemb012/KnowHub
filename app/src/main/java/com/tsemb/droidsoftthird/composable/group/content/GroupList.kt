package com.tsemb.droidsoftthird.composable.group.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tsemb.droidsoftthird.R
import com.tsemb.droidsoftthird.composable.shared.BoldTitleItem
import com.tsemb.droidsoftthird.composable.shared.EmptyMessage
import com.tsemb.droidsoftthird.composable.shared.FundamentalSheet
import com.tsemb.droidsoftthird.model.domain_model.ApiGroup
import com.tsemb.droidsoftthird.model.presentation_model.LoadState

@Composable
fun MyPageGroupList(groupsLoadState: LoadState, navigate: (String) -> Unit) {

    val groups = groupsLoadState.getValueOrNull<List<ApiGroup>>()
    val isLoading = groupsLoadState is LoadState.Loading
    val error = groupsLoadState.getErrorOrNull()

    FundamentalSheet(
        content = { MyPageGroupListContent(isLoading, groups, navigate) },
        isLoading = isLoading,
        error = error
    )
}

@Composable
private fun MyPageGroupListContent(
    isLoading: Boolean,
    groups: List<ApiGroup>?,
    navigate: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
    ) {

        item { BoldTitleItem("マイページ", Modifier.padding(bottom = 8.dp)) }
        if (!isLoading && groups?.isEmpty() == true) item { EmptyMessage(R.string.join_or_create_group) }
        groups?.let { list ->
            items(list.size) { GroupListItem(groups[it], navigate) }
        }

    }
}

