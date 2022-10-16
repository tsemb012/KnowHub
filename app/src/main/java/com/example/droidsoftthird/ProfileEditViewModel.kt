package com.example.droidsoftthird

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.Area
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.fire_model.LoadState
import com.example.droidsoftthird.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(private val useCase: ProfileUseCase): ViewModel( ) {

    private val _rawUserDetail by lazy { MutableLiveData<UserDetail>() }
    private val _editedUserDetail by lazy {
        MediatorLiveData<UserDetail>().also { result ->
            result.addSource(userName) { result.value = result.value?.copy(userName = it) }
            result.addSource(comment) { result.value = result.value?.copy(comment = it) }
        }
    }
    private val _temporalUserImage by lazy { MutableLiveData<Uri>() }
    private val _temporalBackgroundImage by lazy { MutableLiveData<Uri>() }
    private val _loadState by lazy { MutableLiveData<LoadState>() }//TODO LoadStateを改造し、Messageを内包させ、Ui側でProgressとToastで表現する。


    val uiModel by lazy {
        combine(
                ProfileUiModel(),
                _rawUserDetail,
                _editedUserDetail,
                _temporalUserImage,
                _temporalBackgroundImage,
                _loadState,
        ) { current, _rawUserDetail, _editedUserDetail, _temporalUserImage, _temporalBackgroundImage, _loadState ->
            ProfileUiModel(current, _rawUserDetail, _editedUserDetail, _temporalUserImage, _temporalBackgroundImage, _loadState)
        }
    }

    fun fetchUserDetail() {
        val job = viewModelScope.launch {
            runCatching { useCase.fetchUserDetail() }
                .onSuccess {
                    _rawUserDetail.value = it
                    _temporalUserImage.value = it.userImage.toUri()
                    _temporalBackgroundImage.value = it.backgroundImage.toUri()
                    _loadState.value = LoadState.Loaded(it)
                }
                .onFailure { _loadState.value = LoadState.Error(it) }
        }
        _loadState.value = LoadState.Loading(job)
        job.start()
    }

    fun submitProfile() {
        val job =
            viewModelScope.launch {
                runCatching {
                    val userImagePath = async { _temporalUserImage.value?.let { useCase.uploadImage(it) } }
                    val backgroundImagePath = async { _temporalBackgroundImage.value?.let { useCase.uploadImage(it) } }
                    val userDetail = _editedUserDetail.value?.copy(userImage = userImagePath.await().toString(), backgroundImage = backgroundImagePath.await().toString())
                    userDetail?.let { useCase.updateUserDetail(it) }
                }
                    .onSuccess { _loadState.value = LoadState.Loaded(it!!) }
                    .onFailure { _loadState.value = LoadState.Error(it) }
            }
        _loadState.value = LoadState.Loading(job)
        job.start()
    }

    val userName: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val comment: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    fun postAge(age: Int) { _editedUserDetail.value = _editedUserDetail.value?.copy(age = age) }
    fun postArea(area: Area) { _editedUserDetail.value = _editedUserDetail.value?.copy(area = area) }
    fun postGender(gender: UserDetail.Gender) { _editedUserDetail.value = _editedUserDetail.value?.copy(gender = gender.name.lowercase()) }
    fun storeTemporalUserImage(uri: Uri) { _temporalUserImage.value = uri }
    fun storeTemporalBackgroundImage(uri: Uri) { _temporalBackgroundImage.value = uri }

}
