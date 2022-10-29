package com.example.droidsoftthird

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.Area
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.domain_model.initializedUserDetail
import com.example.droidsoftthird.model.fire_model.LoadState
import com.example.droidsoftthird.utils.combine
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

open class ProfileSubmitViewModel (private val useCase: ProfileUseCase): ViewModel() {

    protected val rawUserDetail by lazy { MutableLiveData(initializedUserDetail) }
    private val _editedUserDetail by lazy { MutableLiveData(initializedUserDetail) }
    protected val temporalUserImage by lazy { MutableLiveData("".toUri()) }
    protected val temporalBackgroundImage by lazy { MutableLiveData("".toUri()) }
    private val isTextFilled by lazy { MediatorLiveData<Boolean>().apply {
        addSource(bindingUserName) { value = it.isNullOrBlank() }
        addSource(bindingComment) { value = it.isNullOrBlank() }
    } }
    protected val loadState by lazy { MutableLiveData<LoadState>(LoadState.Initialized) }//TODO LoadStateを改造し、Messageを内包させ、Ui側でProgressとToastで表現する。

    val uiModel by lazy {
        combine(
                ProfileUiModel(),
                rawUserDetail,
                _editedUserDetail,
                temporalUserImage,
                temporalBackgroundImage,
                isTextFilled,
                loadState,
        ) { current, _rawUserDetail, _editedUserDetail, _temporalUserImage, _temporalBackgroundImage, isFilledText, _loadState ->
            ProfileUiModel(current, _rawUserDetail, _editedUserDetail, _temporalUserImage, _temporalBackgroundImage, isFilledText, _loadState)
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
                    val userDetail = buildUserDetail(userImagePath, backgroundImagePath)
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

    private suspend fun buildUserDetail(
            userImagePath: Deferred<Result<StorageReference>?>,
            backgroundImagePath: Deferred<Result<StorageReference>?>,
    ) = uiModel.value?.editedUserDetail?.copy(
            userImage = userImagePath.await().toString(),
            backgroundImage = backgroundImagePath.await().toString(),
            userName = bindingUserName.value ?: throw Exception("userName is null"),
            comment = bindingComment.value ?: throw Exception("comment is null"),
    )

    enum class SubmitType { EDIT, CREATE }

    val bindingUserName: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val bindingComment: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    fun postAge(age: Int) { _editedUserDetail.value = _editedUserDetail.value?.copy(age = age) }
    fun postArea(area: Area) { _editedUserDetail.value = _editedUserDetail.value?.copy(area = area) }
    fun postGender(gender: UserDetail.Gender) { _editedUserDetail.value = _editedUserDetail.value?.copy(gender = gender.name.lowercase()) }
    fun storeTemporalUserImage(uri: Uri) { temporalUserImage.value = uri }
    fun storeTemporalBackgroundImage(uri: Uri) { temporalBackgroundImage.value = uri }

}
