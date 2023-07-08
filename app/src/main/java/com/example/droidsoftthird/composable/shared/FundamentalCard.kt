package com.example.droidsoftthird.composable.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun FundamentalCard(
    onClick: () -> Unit?,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp,
        onClick = { onClick() },
        backgroundColor = colorResource(id = R.color.base_100),
    ) {
        content()
    }
}