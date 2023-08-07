package com.tsemb.droidsoftthird.composable.shared.chip_flow

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tsemb.droidsoftthird.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChipItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) colorResource(id = R.color.primary_dark) else Color.DarkGray
    class CustomRippleTheme(private val rippleColor: Color) : RippleTheme {

        @Composable
        override fun defaultColor() =
            RippleTheme.defaultRippleColor(
                rippleColor,
                lightTheme = MaterialTheme.colors.isLight
            )

        @Composable
        override fun rippleAlpha(): RippleAlpha =
            RippleTheme.defaultRippleAlpha(
                rippleColor.copy(alpha = 0.75f),
                lightTheme = MaterialTheme.colors.isLight
            )
    }

    MaterialTheme {
        CompositionLocalProvider(
            LocalRippleTheme provides CustomRippleTheme(colorResource(id = R.color.primary_dark))
        ) {
            Chip(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                colors = ChipDefaults.chipColors(
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.onSurface,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = color
                ),
                onClick = onClick
            ) {
                Text(text = label, color = color, fontSize = 16.sp)
            }
        }
    }
}