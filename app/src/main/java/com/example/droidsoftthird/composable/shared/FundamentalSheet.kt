package com.example.droidsoftthird.composable.shared

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun FundamentalSheet(content: @Composable () -> Unit, isLoading: Boolean, error: Throwable?) {
    Column {
        Box {
            content()
            if (isLoading) {
                CommonLinearProgressIndicator(Modifier.align(Alignment.TopCenter))
            }
        }
    }
    error?.let { Toast.makeText(LocalContext.current, it.message, Toast.LENGTH_SHORT).show() }
}