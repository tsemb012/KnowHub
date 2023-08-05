package com.example.droidsoftthird.ui.entrance.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.profile.AppendixButton
import com.example.droidsoftthird.composable.shared.SharedConfirmButton
import com.example.droidsoftthird.ui.entrance.state.ConfirmPasswordState
import com.example.droidsoftthird.ui.entrance.state.EmailState
import com.example.droidsoftthird.ui.entrance.state.PasswordState
import com.example.droidsoftthird.utils.compose.supportWideScreen
import com.example.droidsoftthird.utils.webview.openUrl

sealed class SignUpEvent {
    object SignIn : SignUpEvent()
    data class SignUp(val email: String, val password: String) : SignUpEvent()
    object SignInAsGuest : SignUpEvent()
    object NavigateBack : SignUpEvent()
}

@Composable
fun SignUpScreen(onNavigationEvent: (SignUpEvent) -> Unit) {
    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = "",
                onBackPressed = { onNavigationEvent(SignUpEvent.NavigateBack) }
            )
        },
        content = { contentPadding ->
            SignInSignUpScreen(
                contentPadding = contentPadding,
                modifier = Modifier.supportWideScreen()
            ) {
                Column {
                    SignUpContent(
                        onSignUpSubmitted = { email, password ->
                            onNavigationEvent(SignUpEvent.SignUp(email, password))
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun SignUpContent(
    onSignUpSubmitted: (email: String, password: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val passwordFocusRequest = remember { FocusRequester() }
        val confirmationPasswordFocusRequest = remember { FocusRequester() }
        val emailState = remember { EmailState() }

        BrandLogo()

        Spacer(modifier = Modifier.height(64.dp))

        Email(emailState, onImeAction = { passwordFocusRequest.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))
        val passwordState = remember { PasswordState() }
        Password(
            label = stringResource(id = R.string.new_password),
            passwordState = passwordState,
            imeAction = ImeAction.Next,
            onImeAction = { confirmationPasswordFocusRequest.requestFocus() },
            modifier = Modifier.focusRequester(passwordFocusRequest)
        )

        Spacer(modifier = Modifier.height(16.dp))
        val confirmPasswordState = remember { ConfirmPasswordState(passwordState = passwordState) }
        Password(
            label = stringResource(id = R.string.confirm_new_password),
            passwordState = confirmPasswordState,
            onImeAction = { onSignUpSubmitted(emailState.text, passwordState.text) },
            modifier = Modifier.focusRequester(confirmationPasswordFocusRequest)
        )

        Spacer(modifier = Modifier.height(16.dp))

        /*CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = stringResource(id = R.string.terms_and_conditions),
                style = MaterialTheme.typography.body1
            )
        }
        Spacer(modifier = Modifier.height(16.dp))*/
        val context = LocalContext.current
        val termsOfUseUrl = "https://sites.google.com/view/workandchillapp-termofuse/%E3%83%9B%E3%83%BC%E3%83%A0"
        val privacyPolicyUrl = "https://sites.google.com/view/workandchillapp-privacypolicy/%E3%83%9B%E3%83%BC%E3%83%A0"
        Button(
            onClick = { openUrl(context, termsOfUseUrl) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.elevation(0.dp),
            modifier = Modifier.wrapContentHeight()
        ) {
            Text(
                text = "Term Of Use",
                color = Color.Gray,
                style = MaterialTheme.typography.caption,
            )
        }

        Button(
            onClick = { openUrl(context, privacyPolicyUrl) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.elevation(0.dp),
            modifier = Modifier.wrapContentHeight()
        ) {
            Text(
                text = "Privacy Policy",
                color = Color.Gray,
                style = MaterialTheme.typography.caption,
            )
        }
        /*AppendixButton("Term Of Use",  MaterialTheme.typography.body1) { openUrl(context, termsOfUseUrl) }
        AppendixButton("Privacy Policy",  MaterialTheme.typography.body1) { openUrl(context, privacyPolicyUrl) }
        Divider()
*/
        Spacer(modifier = Modifier.height(16.dp))

        SharedConfirmButton(
            text = stringResource(id = R.string.create_account),
            isEditable = emailState.isValid && passwordState.isValid && confirmPasswordState.isValid,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            onSignUpSubmitted(emailState.text, passwordState.text)
        }

        Spacer(modifier = Modifier.height(32.dp))

    }
}

@Composable
fun LinkedText() {
    val annotatedText = AnnotatedString.Builder().apply {
        // 通常のテキストを追加
        append("利用規約に同意してください。詳細は")

        // リンクとして追加したいテキスト部分を定義
        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
            // クリック可能な部分を注釈として追加
            pushStringAnnotation(tag = "URL", annotation = "https://www.example.com/terms")
            append("こちら")
            pop()
        }

        append("をご確認ください。")
    }.toAnnotatedString()

    ClickableText(text = annotatedText) { offset ->
        annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
            .firstOrNull()
            ?.let { annotation ->
                // ここでリンクを開く処理を追加
                println("リンクがクリックされました: ${annotation.item}")
            }
    }
}

@Preview(widthDp = 1024)
@Composable
fun SignUpPreview() {
    MaterialTheme {
        SignUpScreen {}
    }
}