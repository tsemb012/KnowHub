package com.example.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import com.example.droidsoftthird.composable.map.OSMBottomSheetScreen

class LicenseFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                AndroidView(factory = ::WebView, modifier = Modifier.fillMaxSize()) { webView ->
                    with(webView) {
                        loadUrl("file:///android_asset/licenses.html")
                    }
                }
            }
        }
    }
}

