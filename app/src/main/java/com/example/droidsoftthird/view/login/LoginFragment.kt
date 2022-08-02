package com.example.droidsoftthird.view.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply { 
            setContent { MessageCard("JetpackCompose") }
        }
    }

    @Composable
    private fun MessageCard(s: String) {
        Text(text = "Hello $s")
    }

    @Preview
    @Composable
    fun PreviewMessageCard() {
        MessageCard(s = "JetPackCompose")
    }//パラメータを受け取らないコンポーズ可能な関数であること
}
