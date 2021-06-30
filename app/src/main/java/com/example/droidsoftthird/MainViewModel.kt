package com.example.droidsoftthird

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.droidsoftthird.model.UserProfile
import com.example.droidsoftthird.repository.UserGroupRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class MainViewModel @ViewModelInject constructor(private val repository: UserGroupRepository): ViewModel() {

    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?>
        get() = _userProfile

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

    fun clearUserProfile() {
        _userProfile.postValue(null)
    }

    fun getUser(){
        viewModelScope.launch {
            val result = try{
                repository.getUserProfile()
            } catch(e: Exception){
                Result.Error(Exception("Network request failed"))
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
