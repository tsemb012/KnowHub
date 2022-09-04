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

    //TODO ここの全体の処理をリファクタリングする。
    private val _navigateTo = MutableLiveData<Event<Screen>>()
    val navigateTo: LiveData<Event<Screen>>
        get() = _navigateTo

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.signUp(email, password)
            }.onSuccess {
                when(it) {
                    is Result.Success -> {
                        signUpByTokenId(it.data)//TODO ヘッダーからトークンを送るように変更する。
                    }
                    is Result.Failure -> {
                        _error.value = it.exception.message ?: "Error"
                        _navigateTo.value = Event(Screen.Welcome)
                    }
                }
            }.onFailure {
                _error.value = it.message ?: "Unknown error"
            }
        }
    }

    private fun signUpByTokenId(tokenId: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.certifyTokenId(tokenId)
            }.onSuccess {
                saveTokenId(tokenId)
            }.onFailure {
                //TODO ログアウト
                _error.value = it.message ?: "Error"
                _navigateTo.value = Event(Screen.Welcome)
            }
        }
    }

    private fun saveTokenId(tokenId: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.saveTokenId(tokenId)
                _navigateTo.value = Event(Screen.Home)//TODO プロフィールページに行くか。画面遷移を検討する。
            }.onFailure {
                //TODO ログアウト
                _error.value = it.message ?: "Error"
                _navigateTo.value = Event(Screen.Welcome)
            }
        }
    }

    fun signIn() {
        _navigateTo.value = Event(Screen.SignIn)
    }
}
