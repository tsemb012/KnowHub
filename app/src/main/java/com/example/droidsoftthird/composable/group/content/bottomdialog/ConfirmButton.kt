package com.example.droidsoftthird.composable.group.content.bottomdialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R
import com.example.droidsoftthird.model.domain_model.ApiGroup

@Composable
fun ConfirmButton(
    text : String,
    temporalCondition: MutableState<ApiGroup.FilterCondition>,
    onConfirm: (ApiGroup.FilterCondition) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            colors = buttonColors(
                backgroundColor = colorResource(id = R.color.primary_dark)
            ),
            shape = RoundedCornerShape(8.dp),
            onClick = { onConfirm(temporalCondition.value) },
            modifier = Modifier.wrapContentWidth().padding(end = 24.dp, top = 8.dp, bottom = 12.dp)
        ) {
            Text(text, color = Color.White, style = MaterialTheme.typography.h6)
        }
    }
}
