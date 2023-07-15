package com.example.droidsoftthird.composable.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.droidsoftthird.R

@Composable
fun CommonLinearProgressIndicator(modifier: Modifier = Modifier) {
    LinearProgressIndicator(
        color = colorResource(id = R.color.primary_dark),
        trackColor = colorResource(id = R.color.base_100),
        modifier = modifier.fillMaxWidth(),
    )
}