package com.example.droidsoftthird.composable.entrance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R

@Composable
fun InstructionPage(drawable: Int) {
    Image(
        painter = painterResource(id = drawable),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun InstructionPage1 () {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.instruction1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        val baseColor = colorResource(id = R.color.base_100)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            baseColor.copy(alpha = 0.0f), // 開始色
                            baseColor.copy(alpha = 0.0f),
                            baseColor.copy(alpha = 0.0f),
                            baseColor.copy(alpha = 0.5f),
                            baseColor,
                            baseColor,
                            baseColor // 終了色
                        ),
                        //stops = listOf(0.0f, 0.5f, 1.0f), // 各色の位置
                        startY = 0.0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        Image(
            painter = painterResource(id = R.drawable.app_welcome), // ここに画像リソースを指定
            contentDescription = null, // アクセシビリティのための説明文
            alignment = Alignment.TopCenter, // 画像を中央に配置
            modifier = Modifier.fillMaxSize(), // 画像が全体に広がるようにする
            contentScale = ContentScale.Fit// 必要に応じて画像をクロップまたは他の方法で調整
        )
    }
}

@Preview
@Composable
fun InstructionPagePreview() {
    InstructionPage1()
}

