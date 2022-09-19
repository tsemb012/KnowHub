package com.example.droidsoftthird.ui.entrance.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.weight(1f, fill = showBranding).animateContentSize())
            AnimatedVisibility(showBranding, Modifier.fillMaxWidth()) { Branding() }
            Spacer(modifier = Modifier.weight(1f, fill = showBranding).animateContentSize())
            SignInCreateAccount(
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        }
    }
}

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
        Text(
            text = "課題：勉強アプリ",//stringResource(id = R.string.default_web_client_id),
            style = MaterialTheme.typography.subtitle1,//TODO ここでアプリのタイトルを入れ込む。
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
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
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) { //下の階層にアルファを伝えている。
            Text(
                text = "サインイン or サインアップ",//stringResource(id = R.string.),//TODO 日本語に変換する。
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 64.dp, bottom = 12.dp)
            )
        }
        val onSubmit = { onEvent(WelcomeEvent.SignIn) }
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 3.dp)
        ) {
            Text(
                text = "サインイン",//stringResource(id = R.string.default_web_client_id),//TODO 日本語に変換する。
                style = MaterialTheme.typography.subtitle2
            )
        }
        OrSignUp(
            onSignedUp = { onEvent(WelcomeEvent.SignUp) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "Welcome light theme")
@Composable
fun WelcomeScreenPreview() {
    MaterialTheme {
        WelcomeScreen {}
    }
}