package com.tsemb.droidsoftthird.composable.profile

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun LicenseScreen() {
    AndroidView(factory = ::WebView, modifier = Modifier.fillMaxSize()) { webView ->
        with(webView) {
            loadUrl("file:///android_asset/licenses.html")
        }
    }
}