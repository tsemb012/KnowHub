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
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.model.presentation_model.ProfileUiModel
import com.example.droidsoftthird.usecase.ProfileUseCase
import com.example.droidsoftthird.utils.combine
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate

open class ProfileSubmitViewModel (private val useCase: ProfileUseCase): ViewModel() {

    companion object {
        private const val IMAGE_SIZE = "_200x200"
        const val URI_FOR_UPDATE = "URI_FOR_UPDATE"
        const val REF_FOR_INITIALIZE = "REF_FOR_INITIALIZE"
    }

    protected val rawUserDetail by lazy { MutableLiveData(initializedUserDetail) }
    private val _editedUserDetail by lazy { MutableLiveData(initializedUserDetail) }
    protected val temporalUserImage by lazy { MutableLiveData(mapOf<String, String>()) }
    private val isTextFilled by lazy { MediatorLiveData<Boolean>().apply {
        addSource(bindingUserName) { value = !it.isNullOrBlank() && !bindingComment.value.isNullOrBlank() }
        addSource(bindingComment) { value = !bindingComment.value.isNullOrBlank() && !it.isNullOrBlank() }
    } }
    protected val loadState by lazy { MutableLiveData<LoadState>(LoadState.Initialized) }//TODO LoadStateを改造し、Messageを内包させ、Ui側でProgressとToastで表現する。

    val uiModel by lazy {
        combine(
                ProfileUiModel(),
                rawUserDetail,
                _editedUserDetail,
                temporalUserImage,
                isTextFilled,
                loadState,
        ) { current, _rawUserDetail, _editedUserDetail, _temporalUserImage, isFilledText, _loadState ->
            ProfileUiModel(current, _rawUserDetail, _editedUserDetail, _temporalUserImage, isFilledText, _loadState)
        }
    }

    fun submitProfile(type: SubmitType) {
        val job =
            viewModelScope.launch {
                val uri =  uiModel.value?.temporalUserImage?.get(URI_FOR_UPDATE)?.toUri()
                val currentRef = uiModel.value?.temporalUserImage?.get(REF_FOR_INITIALIZE)
                //TODO ここはValidationに追加する必要がある。
                if (uri == null && currentRef == null) return@launch

                runCatching {
                    val authUserDetail = userProfileChangeRequest {
                        displayName = uiModel.value?.editedUserDetail?.userName
                        photoUri = uri
                    }
                    val newRef = async { uri?.let { useCase.uploadImage(uri).path.plus(IMAGE_SIZE)} ?: null }
                    useCase.updateAuthProfile(authUserDetail)//TODO ここなんか変かも
                    val userDetail = buildUserDetail(newRef.await(), currentRef)
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

    private fun buildUserDetail(newRef: String?, currentRef: String?) =
        uiModel.value?.editedUserDetail?.let { detail ->
            detail.copy(
                    userImage = newRef ?: currentRef ?: throw IllegalStateException("Failed to build user detail."),
                    userName = bindingUserName.value ?: throw Exception("userName is null"),
                    comment = bindingComment.value ?: throw Exception("comment is null"),
            )
        }

    enum class SubmitType { EDIT, CREATE }

    val bindingUserName: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    val bindingComment: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    fun postBirthday(age: LocalDate) { _editedUserDetail.value = _editedUserDetail.value?.copy(birthday = age) }
    fun postArea(area: Area) { _editedUserDetail.value = _editedUserDetail.value?.copy(area = area) }
    fun postGender(gender: UserDetail.Gender) { _editedUserDetail.value = _editedUserDetail.value?.copy(gender = gender.name.lowercase()) }
    fun storeTemporalUserImage(uri: Uri) { temporalUserImage.value = mapOf(URI_FOR_UPDATE to uri.toString())  }
}
