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

    enum class LoginState { LOGGED_IN, LOGGED_OUT }

    fun logout() { _loginState.postValue(LoginState.LOGGED_OUT) }

    fun withdraw() { TODO("退会のロジックを記述する。") }

    fun clearTokenCache() {
        viewModelScope.launch {
            repository.clearTokenId()
        }
    }
}
