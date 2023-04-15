package com.example.droidsoftthird.ui.entrance.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.R
import com.example.droidsoftthird.ui.entrance.Screen
import com.example.droidsoftthird.ui.entrance.composable.SignUp
import com.example.droidsoftthird.ui.entrance.composable.SignUpEvent
import com.example.droidsoftthird.ui.entrance.navigate
import com.example.droidsoftthird.vm.entrance.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment: Fragment() {

    private val viewModel: SignUpViewModel by viewModels ()

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
                    SignUp(
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
        }
    }
}