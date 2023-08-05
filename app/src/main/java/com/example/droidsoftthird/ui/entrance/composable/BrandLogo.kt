package com.example.droidsoftthird.ui.entrance.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R

@Composable
fun BrandLogo() {
    val fontName = GoogleFont("Noto Sans Vithkuqi")
    val fontProvider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val fontFamily = FontFamily(
        Font(googleFont = fontName, fontProvider = fontProvider)
    )

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "KnowHub",
            color = Color.Gray,
            style = MaterialTheme.typography.h2,
            fontWeight = FontWeight.Normal,
            fontFamily = fontFamily
        )
        Spacer(modifier = Modifier.height(4.dp))
        val primaryDark = colorResource(id = R.color.primary_dark)
        val accentYellow = colorResource(id = R.color.primary_accent_yellow)
        Canvas(
            modifier = Modifier
                .width(300.dp)
                .height(10.dp)
        ) {
            drawLine(
                color = primaryDark,
                start = Offset.Zero,
                end = Offset(size.width * 0.57f, 0f),
                strokeWidth = size.height
            )
            drawLine(
                color = accentYellow,
                start = Offset(size.width * 0.57f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = size.height
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF)
@Composable
fun BrandLogoPreview() {
    Box(modifier = Modifier.fillMaxWidth().background(color = colorResource(id = R.color.base_100)), contentAlignment = Alignment.Center) {
        BrandLogo()
    }
}