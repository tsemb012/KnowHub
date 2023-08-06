package com.tsemb.droidsoftthird.composable.shared

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.droidsoftthird.R

@Composable
fun FundamentalSheet(content: @Composable () -> Unit, isLoading: Boolean, error: Throwable?) {
        Box (Modifier.background(color = colorResource(id = R.color.base_100))) {
            content()
            if (isLoading) {
                CommonLinearProgressIndicator(Modifier.align(Alignment.TopCenter))
            }
        }
    error?.let { Toast.makeText(LocalContext.current, it.message, Toast.LENGTH_SHORT).show() }
}