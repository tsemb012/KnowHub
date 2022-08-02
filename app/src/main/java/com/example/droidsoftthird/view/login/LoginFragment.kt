package com.example.droidsoftthird.view.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.example.droidsoftthird.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply { 
            setContent { MessageCard(Message("JetpackCompose", "Android")) }
        }
    }
    
    data class Message(val component: String, val os:String)

    @Composable
    private fun MessageCard(msg: Message) {
        Row{
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "picture",
            )
            Column {
                Text(text = msg.os)
                Text(text = msg.component)
            }

        }
    }

    @Preview
    @Composable
    fun PreviewMessageCard() {
        MessageCard(msg = Message("JetpackCompose", "Android"))
    }//パラメータを受け取らないコンポーズ可能な関数であること
}
