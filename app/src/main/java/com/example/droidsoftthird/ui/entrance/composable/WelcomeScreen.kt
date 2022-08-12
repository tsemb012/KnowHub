package com.example.droidsoftthird.ui.entrance.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.ui.entrance.EmailState
import com.example.droidsoftthird.ui.entrance.EmailStateSaver
import com.example.droidsoftthird.R


sealed class WelcomeEvent {
    data class SignIn(val email: String) : WelcomeEvent()
    object SignInAsGuest : WelcomeEvent()//将来的に必要になる可能性があるので残しておく。
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
                onFocusChange = { focused -> showBranding = !focused },
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
            text = stringResource(id = R.string.default_web_client_id),
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
        R.drawable.ic_broken_image_white_24dp//TODO ここを適切なロゴに切り替える。
    } else {
        R.drawable.ic_broken_image_white_24dp
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
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val emailState by rememberSaveable(stateSaver = EmailStateSaver) {//rememberSaveableはActivityが破棄されても値を保持する。
        mutableStateOf(EmailState())//状態を保存するためにListSaverをしようしている。
    }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) { //下の階層にアルファを伝えている。
            Text(
                text = stringResource(id = R.string.default_web_client_id),//TODO 日本語に変換する。
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 64.dp, bottom = 12.dp)
            )
        }
        val onSubmit = {
            if (emailState.isValid) {
                onEvent(WelcomeEvent.SignIn(emailState.text))
            } else {
                emailState.enableShowErrors()
            }
        }
        onFocusChange(emailState.isFocused)
        Email(emailState = emailState, imeAction = ImeAction.Done, onImeAction = onSubmit)
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 3.dp)
        ) {
            Text(
                text = stringResource(id = R.string.default_web_client_id),//TODO 日本語に変換する。
                style = MaterialTheme.typography.subtitle2
            )
        }
        OrSignUp(
            onSignedUp = { onEvent(WelcomeEvent.SignUp) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private  fun Modifier.supportWideScreen() = this
    .fillMaxWidth()
    .wrapContentWidth(align = Alignment.CenterHorizontally)
    .widthIn(max = 840.dp)

@Preview(name = "Welcome light theme")
@Composable
fun WelcomeScreenPreview() {
    MaterialTheme {
        WelcomeScreen {}
    }
}