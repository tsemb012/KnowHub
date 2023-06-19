package com.example.droidsoftthird.composable.group.content.bottomdialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R
import com.example.droidsoftthird.model.domain_model.ApiGroup

@Composable
fun headerRow(
    title: String,
    onCancel: () -> Unit,
    temporalCondition: MutableState<ApiGroup.FilterCondition>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCancel, modifier = Modifier.align(Alignment.CenterVertically)) {
            Icon(Icons.Filled.Close, contentDescription = "キャンセル")
        }
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(id = R.string.clear_filter),
            color = colorResource(id = R.color.primary_dark),
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .clickable(onClick = {
                    temporalCondition.value = ApiGroup.FilterCondition()
                })
        )
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showSystemUi = true, showBackground = true)
@Composable
fun PreviewHeaderRow() {
    val temporalCondition = remember { mutableStateOf(ApiGroup.FilterCondition()) }
    headerRow(
        title = "フィルター",
        onCancel = {},
        temporalCondition = temporalCondition
    )
}