package com.example.droidsoftthird

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.droidsoftthird.model.UserProfile
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class HomeViewModel @ViewModelInject constructor(private val repository: BaseRepositoryImpl<Any?>): ViewModel() {


    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }


    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?>
        get() = _userProfile


    fun getUser(){
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
                        Timber.tag("check_").d(_userProfile.postValue(result.data).toString())
                    } ?: run {
                        _userProfile.postValue(null)
                    }
                }
                //TODO　is Result.Error -> 取得失敗時のエラー記入
            }
        }
    }
}