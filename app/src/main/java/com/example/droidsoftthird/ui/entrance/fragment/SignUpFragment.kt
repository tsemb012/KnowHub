package com.example.droidsoftthird.ui.entrance.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.R
import com.example.droidsoftthird.ui.entrance.Screen
import com.example.droidsoftthird.composable.entrance.SignUpScreen
import com.example.droidsoftthird.composable.entrance.SignUpEvent
import com.example.droidsoftthird.ui.entrance.navigate
import com.example.droidsoftthird.vm.entrance.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment: Fragment() {

    private val viewModel: SignUpViewModel by viewModels ()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let { navigateTo ->
                navigate(navigateTo, Screen.SignUp)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        return ComposeView(requireContext()).apply {
            id = R.id.signUpFragment

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setContent {
                MaterialTheme {

                    Box( modifier = Modifier.background(colorResource(id = R.color.base_100)).fillMaxSize())  {
                        val pagerState = rememberPagerState { 5 }
                        HorizontalPager(state = pagerState) { page ->
                            when (page) {
                                0 -> InstructionPage("Instruction 1")
                                1 -> InstructionPage("Instruction 2")
                                2 -> InstructionPage("Instruction 3")
                                3 -> InstructionPage("Instruction 4")
                                4 -> SignUpScreen(
                                    onNavigationEvent = { event ->
                                        when (event) {
                                            is SignUpEvent.SignUp -> {
                                                viewModel.signUp(event.email, event.password)
                                            }

                                            SignUpEvent.SignIn -> {
                                                viewModel.signIn() //TODO サインインに移動するボタンを作成する。
                                            }

                                            SignUpEvent.NavigateBack -> {
                                                activity?.onBackPressedDispatcher?.onBackPressed()
                                            }

                                            else -> {}
                                        }
                                    }
                                )
                            }
                        }
                        Row(horizontalArrangement = Arrangement.Center,  modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()) {
                            repeat(5) { page ->
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    modifier = Modifier.size(10.dp),
                                    shape = CircleShape,
                                    color = if (page == pagerState.currentPage) Color.Gray else Color.LightGray
                                ) {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Indicator(currentPage: Int, pageCount: Int) {

}

@Composable
fun InstructionPage(instruction: String) {
    Text(text = instruction)
}

@Preview
@Composable
fun PreviewInstructionPage() {
    InstructionPage("Instruction 1")
}
