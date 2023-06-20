package com.example.droidsoftthird.composable.group.content

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.shared.EmptyMessage
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.presentation_model.LoadState

@Composable
fun GroupList(groupsLoadState: LoadState, navigate: (String) -> Unit) {

    val groups = groupsLoadState.getValueOrNull<List<ApiGroup>>()
    val isLoading = groupsLoadState is LoadState.Loading
    val error = groupsLoadState.getErrorOrNull()

    Box {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            if (!isLoading && groups?.isEmpty() == true) item { EmptyMessage (R.string.join_or_create_group) }
            groups?.let { list ->
                items(list.size) { GroupListItem(groups[it], navigate) }
            }

        }
        if (isLoading) {
            LinearProgressIndicator(
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(), color = colorResource(id = R.color.primary_dark), trackColor = colorResource(
                    id = R.color.base_100
                ))
        }
    }
    error?.let { Toast.makeText(LocalContext.current, it.message, Toast.LENGTH_SHORT).show() }
}
