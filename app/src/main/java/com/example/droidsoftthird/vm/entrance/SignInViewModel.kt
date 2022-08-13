package com.example.droidsoftthird.vm.entrance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.droidsoftthird.repository.UserRepository
import com.example.droidsoftthird.ui.entrance.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor (private val userRepository: UserRepository) : ViewModel() {

    //TODO プロフィール入力も追加する。
    private val _navigateTo = MutableLiveData<Event<Screen>>()
    val navigateTo: LiveData<Event<Screen>>
        get() = _navigateTo

    fun signIn(email: String, password: String) {
        userRepository.signIn(email, password)
        _navigateTo.value = Event(Screen.Home)
    }

    fun signInAsGuest() {
        userRepository.signInAsGuest()
        _navigateTo.value = Event(Screen.Home)
    }

    fun signUp() {
        _navigateTo.value = Event(Screen.Home)
    }
}
