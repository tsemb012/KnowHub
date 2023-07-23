package com.example.droidsoftthird.composable.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R

@Composable
fun SharedBackButton(navigateUp: () -> Unit) {
    IconButton(
        onClick = { navigateUp() },
        modifier = Modifier
            .background(colorResource(id = R.color.base_100), shape = CircleShape)
            .padding(3.dp)
    ) {
        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
    }
}