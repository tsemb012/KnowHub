package com.example.droidsoftthird.vm.entrance

import androidx.lifecycle.*
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.example.droidsoftthird.ui.entrance.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor (private val repository: BaseRepositoryImpl) : ViewModel() {

    private val _navigateTo = MutableLiveData<Event<Screen>>()
    val navigateTo: LiveData<Event<Screen>>
        get() = _navigateTo
    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>>
        get() = _error

    fun signIn(email: String, password: String) { //TODO ここの中身を書き換える。　→　Firebaseと繋いで
        viewModelScope.launch {
            kotlin.runCatching {
                repository.singIn(email, password)
            }.onSuccess {
                _navigateTo.value = Event(Screen.Home)
            }.onFailure {
                _error.value = it.message?.let { error -> Event(error) }
            }
        }
    }

    fun signUp() {
        _navigateTo.value = Event(Screen.Home)
    }
}
