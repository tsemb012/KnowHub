package com.example.droidsoftthird.composable.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R

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
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            maxLines = 1
        )

        Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)) {

            itemList.forEach { item ->
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (item.icon != null) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.CenterVertically),
                                tint = Color.Gray
                            )
                        } else {
                            Spacer(modifier = Modifier.width(16.dp))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        if (item.isUrl) { // If the item is a URL, use ClickableText
                            ClickableUrlText(url = item.text)
                        } else { // Otherwise, use regular Text
                            Text(
                                text = item.text,
                                style = MaterialTheme.typography.body1,
                                color = Color.DarkGray,
                                maxLines = item.maxLines
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ClickableUrlText(url: String) {
    val uriHandler = LocalUriHandler.current
    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = colorResource(id = R.color.primary_dark)
            )
        ) {
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
    val icon: ImageVector? = null,
    val text: String,
    val maxLines: Int = 1,
    val isUrl: Boolean = false
)