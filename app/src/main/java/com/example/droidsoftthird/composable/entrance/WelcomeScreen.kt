package com.example.droidsoftthird.composable.entrance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
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
    Surface(modifier = Modifier.supportWideScreen()) {
        Box(Modifier.fillMaxSize()) {

            Column (
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BrandLogo()
                Spacer(modifier = Modifier.height(120.dp))
                SignInCreateAccount(
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

        }
    }
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
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp, disabledElevation = 0.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = { onEvent(WelcomeEvent.SignUp) },
            modifier = modifier
                .padding(start = 36.dp, end = 36.dp, top = 8.dp, bottom = 4.dp)
        ) {
            Text(stringResource(id = R.string.sign_up), color = Color.White, style = MaterialTheme.typography.h6, fontWeight = FontWeight.SemiBold)
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.LightGray
            ),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp, disabledElevation = 0.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = { onEvent(WelcomeEvent.SignIn) },
            modifier = modifier
                .padding(start = 36.dp, end = 36.dp, top = 4.dp, bottom = 8.dp)
        ) {
            Text(stringResource(id = R.string.login), color = Color.DarkGray, style = MaterialTheme.typography.h6, fontWeight = FontWeight.SemiBold)
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