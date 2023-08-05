package com.example.droidsoftthird.composable.entrance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.droidsoftthird.R
import com.example.droidsoftthird.composable.shared.SharedConfirmButton
import com.example.droidsoftthird.ui.entrance.state.EmailState
import com.example.droidsoftthird.ui.entrance.state.EmailStateSaver
import com.example.droidsoftthird.ui.entrance.state.PasswordState
import com.example.droidsoftthird.utils.compose.supportWideScreen

//TODO コード汚すぎて吐きそう。時間ないので、後でリファクタ。
class SignInScreen { //TODO 永続のチェックボックスを追加する。
}

sealed class SignInEvent {
    data class SignIn(val email: String, val password: String) : SignInEvent()
    //object SignUp : SignInEvent()　TODO　パスワードを忘れた時の導線を作成しておく。
    //object SignInAsGuest : SignInEvent() いつか使うかもしれないので、残しておく。
    object NavigateBack : SignInEvent()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignInScreen(onNavigationEvent: (SignInEvent) -> Unit) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()//ComposableでCoroutineを扱うときの定番のスコープ

    val snackbarErrorText = stringResource(id = R.string.feature_not_available)//TODO 日本語に変換する
    val snackbarActionLabel = stringResource(id = R.string.dismiss)

    //基礎的な画面を作ってくれる
    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = "",
                onBackPressed = { onNavigationEvent(SignInEvent.NavigateBack) }
            )
        },
        backgroundColor = colorResource(id = R.color.base_100),
        content = { contentPadding ->


            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BrandLogo()
                SignInSignUpScreen(//TODO ここに置いてあるのはなぜ？
                    modifier = Modifier.supportWideScreen(),
                    contentPadding = contentPadding,
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SignInContent(
                            onSignInSubmitted = { email, password ->
                                onNavigationEvent(SignInEvent.SignIn(email, password))
                            }
                        )
                        /*Spacer(modifier = Modifier.height(16.dp))
                        TextButton(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = snackbarErrorText,
                                        actionLabel = snackbarActionLabel
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.forgot_password))
                        }*/
                    }
                }
            }


        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ErrorSnackbar(
            snackbarHostState = snackbarHostState,
            onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun SignInContent(//TODO passwordConfirmationも増やさないとけいないかも
    onSignInSubmitted: (email: String, password: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }//Focusされている時の状態を保存
        val emailState by rememberSaveable(stateSaver = EmailStateSaver) {
            mutableStateOf(EmailState())
        }

        Spacer(modifier = Modifier.height(16.dp))

        Email(emailState, onImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))

        val passwordState = remember { PasswordState() }

        val onSubmit = {
            if (emailState.isValid && passwordState.isValid) {
                onSignInSubmitted(emailState.text, passwordState.text)
            }
        }
        Password(
            label = stringResource(id = R.string.password),
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = { onSubmit() }
        )
        Spacer(modifier = Modifier.height(32.dp))

        SharedConfirmButton(
            text = stringResource(id = R.string.login),
            onConfirm = onSubmit,
            isEditable = emailState.isValid && passwordState.isValid,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { }
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.body2
                    )
                },
                action = {
                    data.actionLabel?.let {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(id = R.string.dismiss),
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}

@Preview(name = "Sign in light theme")
@Composable
fun SignInPreview() {
    MaterialTheme {
        SignInScreen {}
    }
}


