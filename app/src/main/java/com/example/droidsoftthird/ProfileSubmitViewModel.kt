package com.example.droidsoftthird

import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.Area
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.fire_model.LoadState
import com.example.droidsoftthird.utils.combine
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

open class ProfileSubmitViewModel (private val useCase: ProfileUseCase): ViewModel() {

    protected val rawUserDetail by lazy { MutableLiveData<UserDetail>() }
    private val _editedUserDetail by lazy {
        MediatorLiveData<UserDetail>().also { result ->
            result.addSource(userName) { result.value = result.value?.copy(userName = it) }
            result.addSource(comment) { result.value = result.value?.copy(comment = it) }
        }
    }
    protected val temporalUserImage by lazy { MutableLiveData<Uri>() }
    protected val temporalBackgroundImage by lazy { MutableLiveData<Uri>() }
    protected val loadState by lazy { MutableLiveData<LoadState>() }//TODO LoadStateを改造し、Messageを内包させ、Ui側でProgressとToastで表現する。


    val uiModel by lazy {
        combine(
            ProfileUiModel(),
            rawUserDetail,
            _editedUserDetail,
            temporalUserImage,
            temporalBackgroundImage,
            loadState,
        ) { current, _rawUserDetail, _editedUserDetail, _temporalUserImage, _temporalBackgroundImage, _loadState ->
            ProfileUiModel(current, _rawUserDetail, _editedUserDetail, _temporalUserImage, _temporalBackgroundImage, _loadState)
        }
    }

    fun submitProfile(type: SubmitType) {
        val job =
            viewModelScope.launch {
                runCatching {
                    val authUserDetail = userProfileChangeRequest {
                        displayName = uiModel.value?.editedUserDetail?.userName
                        photoUri = uiModel.value?.temporalUserImage
                    }
                    val userImagePath = async { temporalUserImage.value?.let { useCase.uploadImage(it) } }
                    val backgroundImagePath = async { temporalBackgroundImage.value?.let { useCase.uploadImage(it) } }
                    useCase.updateAuthProfile(authUserDetail)//TODO ここなんか変かも
                    val userDetail = _editedUserDetail.value?.copy(userImage = userImagePath.await().toString(), backgroundImage = backgroundImagePath.await().toString())
                    userDetail?.let {
                        when (type) {
                            SubmitType.CREATE -> useCase.createUserDetail(it)
                            SubmitType.EDIT -> useCase.updateUserDetail(it)
                        }
                    }
                }
                    .onSuccess { loadState.value = LoadState.Loaded(it!!) }
                    .onFailure { loadState.value = LoadState.Error(it) }
            }
        loadState.value = LoadState.Loading(job)
        job.start()
    }

    enum class SubmitType { EDIT, CREATE }

    val userName: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val comment: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    fun postAge(age: Int) { _editedUserDetail.value = _editedUserDetail.value?.copy(age = age) }
    fun postArea(area: Area) { _editedUserDetail.value = _editedUserDetail.value?.copy(area = area) }
    fun postGender(gender: UserDetail.Gender) { _editedUserDetail.value = _editedUserDetail.value?.copy(gender = gender.name.lowercase()) }
    fun storeTemporalUserImage(uri: Uri) { temporalUserImage.value = uri }
    fun storeTemporalBackgroundImage(uri: Uri) { temporalBackgroundImage.value = uri }

}
