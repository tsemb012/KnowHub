package com.example.droidsoftthird.composable.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun SharedDescriptions(
    title: String,
    itemList: List<DescriptionItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            color = Color.DarkGray,
            maxLines = 1
        )

        Column(modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)) {

            itemList.forEach { (icon, text, maxLines, isUrl) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterVertically),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    if (isUrl) { // If the item is a URL, use ClickableText
                        ClickableUrlText(url = text)
                    } else { // Otherwise, use regular Text
                        Text(
                            text = text,
                            style = MaterialTheme.typography.body1,
                            color = Color.DarkGray,
                            maxLines = maxLines
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClickableUrlText(url: String) {
    val uriHandler = LocalUriHandler.current
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
            append(url)
            addStringAnnotation(
                tag = "URL",
                annotation = url,
                start = 0,
                end = url.length
            )
        }
    }

    ClickableText(
        text = annotatedText,
        style = MaterialTheme.typography.body1,
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        })
}

data class DescriptionItem(
    val icon: ImageVector,
    val text: String,
    val maxLines: Int,
    val isUrl: Boolean = false
)