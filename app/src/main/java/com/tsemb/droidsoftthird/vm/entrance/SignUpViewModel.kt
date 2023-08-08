package com.tsemb.droidsoftthird.vm.entrance

import androidx.lifecycle.*
import com.tsemb.droidsoftthird.Result
import com.tsemb.droidsoftthird.repository.BaseRepositoryImpl
import com.tsemb.droidsoftthird.ui.entrance.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor (private val repository: BaseRepositoryImpl) : ViewModel() {

    //TODO ここの全体の処理をリファクタリングする。
    private val _navigateTo = MutableLiveData<Event<Screen>>()
    val navigateTo: LiveData<Event<Screen>>
        get() = _navigateTo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun signUp(email: String, password: String) {
        if (isLoading.value == true) return

        val job = viewModelScope.launch {
            kotlin.runCatching {
                repository.signUpAndFetchToken(email, password)
            }.onSuccess {
                when(it) {
                    is Result.Success -> {
                        signUpByTokenId(it.data)//TODO ヘッダーからトークンを送るように変更する。
                    }
                    is Result.Failure -> {
                        _error.value = it.exception.message ?: "Error"
                        _navigateTo.value = Event(Screen.Welcome)
                        _isLoading.value = false
                    }
                }
            }.onFailure {
                _error.value = it.message ?: "サインアップに失敗しました。"
                _navigateTo.value = Event(Screen.Welcome)
                _isLoading.value = false
            }
        }
        _isLoading.value = true
        job.start()
    }

    private fun signUpByTokenId(tokenId: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.certifyAndRegister(tokenId)
            }.onSuccess {
                saveTokenId(tokenId)
            }.onFailure {
                //TODO ログアウト
                _error.value = it.message ?: "Error"
                _navigateTo.value = Event(Screen.Welcome)
                _isLoading.value = false
            }
        }
    }

    private fun saveTokenId(tokenId: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.saveTokenId(tokenId)
            }.onSuccess {
                _navigateTo.value = Event(Screen.CreateProfile)//TODO プロフィールページに行くか。画面遷移を検討する。
                _isLoading.value = false
            }.onFailure {
                //TODO ログアウト
                _error.value = it.message ?: "Error"
                _navigateTo.value = Event(Screen.Welcome)
                _isLoading.value = false
            }
        }
    }
}
