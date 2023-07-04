package com.example.droidsoftthird.composable.group.content

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.shared.BoldTitleItem
import com.example.droidsoftthird.composable.shared.CommonLinearProgressIndicator
import com.example.droidsoftthird.composable.shared.EmptyMessage
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.presentation_model.LoadState

@Composable
fun MyPageGroupList(groupsLoadState: LoadState, navigate: (String) -> Unit) {

    val groups = groupsLoadState.getValueOrNull<List<ApiGroup>>()
    val isLoading = groupsLoadState is LoadState.Loading
    val error = groupsLoadState.getErrorOrNull()

    Column {
        Box {
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
            if (isLoading) {
                CommonLinearProgressIndicator(Modifier.align(Alignment.TopCenter))
            }
        }
    }
    error?.let { Toast.makeText(LocalContext.current, it.message, Toast.LENGTH_SHORT).show() }
}

