package com.example.droidsoftthird

import androidx.lifecycle.*
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: BaseRepositoryImpl): ViewModel() {

    private val _loginState = MutableLiveData(LoginState.LOGGED_IN)
    val loginState: LiveData<LoginState>
        get() = _loginState

    enum class LoginState { LOGGED_IN, LOGGED_OUT, ON_WITHDRAW, DURING_WITHDRAW }

    fun logout() { _loginState.postValue(LoginState.LOGGED_OUT) }

    fun withdraw() { _loginState.postValue(LoginState.ON_WITHDRAW) }

    fun clearTokenCache() {
        viewModelScope.launch {
            repository.clearTokenId()
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            runCatching { repository.deleteUser() }
                .onSuccess { _loginState.postValue(LoginState.DURING_WITHDRAW) }
                .onFailure { throw IllegalStateException("ユーザー削除に失敗しました。") }
        }
    }
}
