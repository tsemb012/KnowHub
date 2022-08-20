package com.example.droidsoftthird.vm.entrance

import androidx.lifecycle.*
import com.example.droidsoftthird.Result
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.example.droidsoftthird.ui.entrance.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor (private val repository: BaseRepositoryImpl) : ViewModel() {

    private val _navigateTo = MutableLiveData<Event<Screen>>()
    val navigateTo: LiveData<Event<Screen>>
        get() = _navigateTo

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.singUp(email, password)
            }.onSuccess {
                when(it) {
                    is Result.Success -> {
                        certifyTokenId(it.data)
                    }
                    is Result.Failure -> {
                        _error.value = it.exception.message ?: "Error"
                    }
                }
                _navigateTo.value = Event(Screen.Home)
            }.onFailure {
                _error.value = it.message ?: "Unknown error"
            }
        }
    }

    private fun certifyTokenId(data: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.certifyTokenId(data)
            }.onSuccess {
/*                when(it) {
                    is Result.Success -> {
                        _navigateTo.value = Event(Screen.Home)
                    }
                    is Result.Failure -> {
                        _error.value = it.exception.message ?: "Error"
                    }
                }*/
            }.onFailure {
                _error.value = it.message ?: "Unknown error"
            }
        }
    }


    fun signIn() {
        _navigateTo.value = Event(Screen.Home)
    }
}
