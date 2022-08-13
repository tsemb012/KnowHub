package com.example.droidsoftthird.ui.entrance.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.R
import com.example.droidsoftthird.ui.entrance.Screen
import com.example.droidsoftthird.vm.entrance.SignInViewModel
import com.example.droidsoftthird.vm.entrance.SignInViewModelFactory
import com.example.droidsoftthird.ui.entrance.composable.SignIn
import com.example.droidsoftthird.ui.entrance.composable.SignInEvent
import com.example.droidsoftthird.ui.entrance.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment: Fragment() {

    private val viewModel: SignInViewModel by viewModels { SignInViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let { navigateTo ->
                navigate(navigateTo, Screen.SignIn)
            }
        }
        return ComposeView(requireContext()).apply {
            id =  R.id.signInFragment

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                MaterialTheme {
                    SignIn(
                        onNavigationEvent = { event ->
                            when(event) {
                                is SignInEvent.SignIn -> { viewModel.signIn(event.email, event.password) }
                                //SignInEvent.SignUp -> { viewModel.TODO("") } //TODO パスワードを忘れた時の導線を作っておく。
                                SignInEvent.NavigateBack -> { activity?.onBackPressedDispatcher?.onBackPressed() }
                            }
                        }
                    )
                }
            }
        }
    }

}