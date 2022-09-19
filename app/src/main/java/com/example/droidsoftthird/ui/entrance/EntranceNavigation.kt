package com.example.droidsoftthird.ui.entrance

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.droidsoftthird.R
import java.security.InvalidParameterException

enum class Screen { Welcome, SignUp, SignIn, Home }

fun Fragment.navigate(to: Screen, from: Screen) {
    if (to == from) {
        throw InvalidParameterException("Can't navigate to $to")
    }
    when (to) {//TODO 後から改築。
        Screen.Welcome -> {
            findNavController().navigate(R.id.welcomeFragment)
        }
        Screen.SignUp -> {
            findNavController().navigate(R.id.signUpFragment)
        }
        Screen.SignIn -> {
            findNavController().navigate(R.id.signInFragment)
        }
        Screen.Home -> {
            findNavController().navigate(R.id.homeFragment)
        }
    }
}
