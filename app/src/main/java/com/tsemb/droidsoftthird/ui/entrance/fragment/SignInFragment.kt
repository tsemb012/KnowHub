package com.tsemb.droidsoftthird.ui.entrance.fragment

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
import com.tsemb.droidsoftthird.ui.entrance.Screen
import com.tsemb.droidsoftthird.vm.entrance.SignInViewModel
import com.tsemb.droidsoftthird.composable.entrance.SignInScreen
import com.tsemb.droidsoftthird.composable.entrance.SignInEvent
import com.tsemb.droidsoftthird.ui.entrance.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment: Fragment() {

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.navigateTo.observe(viewLifecycleOwner) { navigate(Screen.Home, Screen.SignIn) }
        viewModel.error.observe(viewLifecycleOwner) { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        return ComposeView(requireContext()).apply {
            id =  R.id.signInFragment

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setContent {
                MaterialTheme {
                    SignInScreen(
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