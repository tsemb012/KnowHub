package com.example.droidsoftthird

import androidx.lifecycle.*
import com.example.droidsoftthird.model.domain_model.fire_model.FireUserProfile
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: BaseRepositoryImpl): ViewModel() {//TODO ライフサイクルの問題っぽい。

    private val _userProfile = MutableLiveData<FireUserProfile?>() //TODO 削除予定
    val userProfile: LiveData<FireUserProfile?>
        get() = _userProfile

    private val _loginState = MutableLiveData(LoginState.LOGGED_IN)
    val loginState: LiveData<LoginState>
        get() = _loginState
    enum class LoginState { LOGGED_IN, LOGGED_OUT }

    enum class AuthenticationState { //TODO 削除予定
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user -> //TODO 削除予定
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    fun logout() { _loginState.postValue(LoginState.LOGGED_OUT) }

    fun clearUserProfile() {//TODO 削除予定　ユーザーの情報はAPIに渡すので、
        _userProfile.postValue(null)
    }

    fun getUser(){//TODO 削除予定
        viewModelScope.launch {
            val result = try{
                repository.getUserProfile()
            } catch(e: Exception){
                Result.Failure(Exception("Network request failed"))
            }
            when (result) {
                is Result.Success -> {
                    result.data?.let {
                        _userProfile.postValue(result.data)
                    } ?: run {
                        _userProfile.postValue(null)
                    }
                }
                is Result.Failure -> Timber.d("error at ${this@MainViewModel}")
            }
        }
    }

    fun clearTokenCache() {
        viewModelScope.launch {
            repository.clearTokenId()
        }
    }
}
