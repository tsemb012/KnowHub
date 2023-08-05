package com.example.droidsoftthird.composable.entrance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R

@Composable
fun InstructionPage(title: String/*, instruction: String, drawable: Int*/) {
    Column {
        Text(text = title)
        /*Text(text = instruction)
        Image(
            painter = painterResource(id = drawable),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )*/
    }
}
