package com.example.droidsoftthird

import androidx.lifecycle.*
import com.example.droidsoftthird.model.fire_model.UserProfile
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: BaseRepositoryImpl): ViewModel() {

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
