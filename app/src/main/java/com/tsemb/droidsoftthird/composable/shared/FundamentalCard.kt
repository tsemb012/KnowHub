package com.tsemb.droidsoftthird.composable.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            .padding(vertical = 10.dp),
        elevation = 4.dp,
        //border = BorderStroke(0.5.dp, colorResource(id = R.color.primary_light)),
        onClick = { onClick() },
        backgroundColor = Color.White//colorResource(id = R.color.base_100),
    ) {
        content()
    }
}