package com.tsemb.droidsoftthird.ui.entrance.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.R
import com.tsemb.droidsoftthird.composable.entrance.InstructionPage
import com.tsemb.droidsoftthird.ui.entrance.Screen
import com.tsemb.droidsoftthird.composable.entrance.SignUpScreen
import com.tsemb.droidsoftthird.composable.entrance.SignUpEvent
import com.tsemb.droidsoftthird.composable.shared.SharedConfirmButton
import com.tsemb.droidsoftthird.ui.entrance.navigate
import com.tsemb.droidsoftthird.vm.entrance.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

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

                    Box( modifier = Modifier
                        .background(colorResource(id = R.color.base_100))
                        .fillMaxSize())  {
                        val pagerState = rememberPagerState { 6 }
                        HorizontalPager(state = pagerState) { page ->
                            when (page) {
                                0 -> InstructionPage(R.drawable.instruction1)
                                1 -> InstructionPage(R.drawable.instruction2)
                                2 -> InstructionPage(R.drawable.instruction3)
                                3 -> InstructionPage(R.drawable.instruction4)
                                4 -> InstructionPage(R.drawable.instruction5)
                                5 -> SignUpScreen(
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

                        Column (modifier = Modifier.align(Alignment.BottomCenter)) {
                            Divider()
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
                            ) {
                                repeat(6) { page ->
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Surface(
                                        modifier = Modifier.size(10.dp),
                                        shape = CircleShape,
                                        color = if (page == pagerState.currentPage) Color.Gray else Color.LightGray
                                    ) {}
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            if (pagerState.currentPage < 5) {
                                SharedConfirmButton(
                                    text = "次へ",
                                    onConfirm = { runBlocking { pagerState.scrollToPage(pagerState.currentPage + 1) } },
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                            }
                        }
                    }
                }
            }
        }
    }
}
