package com.example.droidsoftthird.ui.entrance.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R
import com.example.droidsoftthird.utils.compose.supportWideScreen


sealed class WelcomeEvent {
    object SignIn : WelcomeEvent()
    object SignUp : WelcomeEvent()
}

@Composable
fun WelcomeScreen(onEvent: (WelcomeEvent) -> Unit) {
    var showBranding by remember { mutableStateOf(true) }

    Surface(modifier = Modifier.supportWideScreen()) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.welcome_screen), // ここに画像リソースを指定
                contentDescription = null, // アクセシビリティのための説明文
                alignment = Alignment.TopCenter, // 画像を中央に配置
                modifier = Modifier.fillMaxSize(), // 画像が全体に広がるようにする
                contentScale = ContentScale.Fit// 必要に応じて画像をクロップまたは他の方法で調整
            )


            SignInCreateAccount(
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

 /*           Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(
                    modifier = Modifier
                        .weight(1f, fill = showBranding)
                        .animateContentSize()
                )
                AnimatedVisibility(showBranding, Modifier.fillMaxWidth()) { Branding() }
                Spacer(
                    modifier = Modifier
                        .weight(1f, fill = showBranding)
                        .animateContentSize()
                )

            }
        }
    }*/


@Composable
private fun Branding(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        Logo(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 76.dp)
        )
    }
}

@Composable
private fun Logo(
    modifier: Modifier = Modifier,
    lightTheme: Boolean = MaterialTheme.colors.isLight
) {
    val assetId = if (lightTheme) {
        R.drawable.ic_baseline_group_24//TODO 書き換える。
    } else {
        R.drawable.ic_baseline_group_24
    }
    Image(
        painter = painterResource(id = assetId),
        modifier = modifier,
        contentDescription = null
    )
}

@Composable
private fun SignInCreateAccount(
    onEvent: (WelcomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.primary_dark)
            ),
            shape = RoundedCornerShape(8.dp),
            onClick = { onEvent(WelcomeEvent.SignUp) },
            modifier = modifier
                .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 12.dp)
        ) {
            Text(stringResource(id = R.string.sign_up), color = Color.White, style = MaterialTheme.typography.h5)
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.LightGray
            ),
            shape = RoundedCornerShape(8.dp),
            onClick = { onEvent(WelcomeEvent.SignIn) },
            modifier = modifier
                .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 12.dp)
        ) {
            Text(stringResource(id = R.string.login), color = Color.DarkGray, style = MaterialTheme.typography.h5)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(name = "Welcome light theme")
@Composable
fun WelcomeScreenPreview() {
    MaterialTheme {
        WelcomeScreen {}
    }
}