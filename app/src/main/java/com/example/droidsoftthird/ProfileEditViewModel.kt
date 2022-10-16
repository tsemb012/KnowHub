package com.example.droidsoftthird

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.Area
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.fire_model.LoadState
import com.example.droidsoftthird.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(private val useCase: ProfileUseCase): ViewModel( ) {

    private val _loadState by lazy { MutableLiveData<LoadState>() }//TODO LoadStateを改造し、Messageを内包させ、Ui側でProgressとToastで表現する。
    private val _rawUserDetail by lazy { MutableLiveData<UserDetail>() }
    private val _editedUserDetail by lazy {
        MediatorLiveData<UserDetail>().also { result ->
            result.addSource(userName) { result.value = result.value?.copy(userName = it) }
            result.addSource(comment) { result.value = result.value?.copy(comment = it) }
            result.addSource(userImage) { result.value = result.value?.copy(userImage = it) }
            result.addSource(backgroundImage) { result.value = result.value?.copy(backgroundImage = it) }
        }
    }

    val uiModel by lazy {
        combine(
                ProfileUiModel(),
                _rawUserDetail,
                _editedUserDetail,
                _loadState,
        ) { current, _rawUserDetail, _editedUserDetail, _loadState ->
            ProfileUiModel(current, _rawUserDetail, _editedUserDetail, _loadState)
        }
    }

    fun fetchUserDetail() {
        val job = viewModelScope.launch {
            runCatching { useCase.fetchUserDetail() }
                .onSuccess {
                    _rawUserDetail.value = it
                    _loadState.value = LoadState.Loaded(it)
                }
                .onFailure { _loadState.value = LoadState.Error(it) }
        }
        _loadState.value = LoadState.Loading(job)
        job.start()
    }

    fun submitProfile() {
        val job = viewModelScope.launch {
            val userDetail = _editedUserDetail.value ?: return@launch
            runCatching { useCase.updateUserDetail(userDetail) }
                .onSuccess { _loadState.value = LoadState.Loaded(it) }
                .onFailure { _loadState.value = LoadState.Error(it) }
        }
        _loadState.value = LoadState.Loading(job)
        job.start()
    }

    val userName: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val comment: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val userImage: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val backgroundImage: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    fun postAge(age: Int) { _editedUserDetail.value = _editedUserDetail.value?.copy(age = age) }
    fun postArea(area: Area) { _editedUserDetail.value = _editedUserDetail.value?.copy(area = area) }
    fun postGender(gender: UserDetail.Gender) { _editedUserDetail.value = _editedUserDetail.value?.copy(gender = gender.name.lowercase()) }

}
