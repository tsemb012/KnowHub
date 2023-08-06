package com.tsemb.droidsoftthird.vm.entrance

import androidx.lifecycle.*
import com.tsemb.droidsoftthird.Result
import com.tsemb.droidsoftthird.repository.BaseRepositoryImpl
import com.tsemb.droidsoftthird.ui.entrance.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor (private val repository: BaseRepositoryImpl) : ViewModel() {
    //TODO リファクタリングして簡略化

    private val _navigateTo = MutableLiveData<Event<Screen>>()
    val navigateTo: LiveData<Event<Screen>>
        get() = _navigateTo
    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.singIn(email, password)
            }.onSuccess {
                when (it) {
                    is Result.Success -> saveTokenId(it.data)
                    is Result.Failure -> _error.value = "ログインに失敗しました。"
                }
            }.onFailure {
                _error.value = it.message ?: "Unknown error"
            }
        }
    }

    private fun saveTokenId(tokenId: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.saveTokenId(tokenId)
                _navigateTo.value = Event(Screen.Home)
            }.onFailure {
                //TODO ログアウト
                _error.value = it.message ?: "Error"
                _navigateTo.value = Event(Screen.Welcome)
            }
        }
    }

    fun signUp() {
        _navigateTo.value = Event(Screen.SignUp)
    }
}
