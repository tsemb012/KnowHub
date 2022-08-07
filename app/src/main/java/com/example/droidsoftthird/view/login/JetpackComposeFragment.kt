package com.example.droidsoftthird.view.login


import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.droidsoftthird.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class JetpackComposeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    Conversation(SampleData.conversationSample)
                }
            }
        }
    }
    
    data class Message(val component: String, val os:String)

    @Composable
    private fun MessageCard(msg: Message) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(R.drawable.fui_ic_anonymous_white_24dp),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    // Set image size to 40 dp
                    .size(40.dp)
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))

            var isExpanded by remember { mutableStateOf(false) }
            val surfaceColor by animateColorAsState(
                if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface
            )


            Column (modifier = Modifier.clickable { isExpanded = !isExpanded }){
                Text(
                    text = msg.os,
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 1.dp,
                    color = surfaceColor,
                    modifier = Modifier.animateContentSize().padding(1.dp)
                ) {
                    Text(
                        text = msg.component,
                        modifier = Modifier.padding(all = 4.dp),
                        maxLines = if(isExpanded) Int.MAX_VALUE else 1,
                        style = MaterialTheme.typography.body2
                    )
                }
            }

        }
    }



    @Composable
    fun Conversation(messages: List<Message>) {
        LazyColumn {
            items(messages) { message ->
                MessageCard(message)
            }
        }
    }

    @Preview
    @Composable
    fun PreviewConversation() {
            Conversation(SampleData.conversationSample)
    }


    @Preview(name = "Light Mode")
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun PreviewMessageCard() {
        MaterialTheme {
            MessageCard(msg = Message("JetpackCompose", "Android"))
        }
    }

    object SampleData {
        val conversationSample = listOf(
            Message(UUID.randomUUID().toString(),UUID.randomUUID().toString()),
            Message(UUID.randomUUID().toString(),UUID.randomUUID().toString()),
            Message(UUID.randomUUID().toString(),UUID.randomUUID().toString()),
            Message(UUID.randomUUID().toString(),UUID.randomUUID().toString()),
            Message(UUID.randomUUID().toString(),UUID.randomUUID().toString()),
            Message(UUID.randomUUID().toString(),UUID.randomUUID().toString()),
            Message(UUID.randomUUID().toString(),UUID.randomUUID().toString()),
            Message(UUID.randomUUID().toString(),UUID.randomUUID().toString()),
            Message(UUID.randomUUID().toString(),UUID.randomUUID().toString()),
            Message(UUID.randomUUID().toString(),UUID.randomUUID().toString()),
            Message(UUID.randomUUID().toString(),UUID.randomUUID().toString()),
            Message(UUID.randomUUID().toString(),UUID.randomUUID().toString()),
        )
    }
}
