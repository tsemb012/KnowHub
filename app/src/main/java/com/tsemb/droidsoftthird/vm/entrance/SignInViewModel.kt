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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    val error: LiveData<String>
        get() = _error

    fun signIn(email: String, password: String) {
        val job = viewModelScope.launch {
            kotlin.runCatching {
                repository.singIn(email, password)
            }.onSuccess {
                when (it) {
                    is Result.Success -> saveTokenId(it.data)
                    is Result.Failure -> _error.value = "ログインに失敗しました。"
                }
            }.onFailure {
                _error.value = it.message ?: "ログインに失敗しました"
                _isLoading.value = false
            }
        }
        _isLoading.value = true
        job.start()
    }

    private fun saveTokenId(tokenId: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.saveTokenId(tokenId)
            }.onSuccess {
                _isLoading.value = false
                _navigateTo.value = Event(Screen.Home)
            }.onFailure {
                _error.value = it.message ?: "Error"
                _navigateTo.value = Event(Screen.Welcome)
            }
        }
    }
}
