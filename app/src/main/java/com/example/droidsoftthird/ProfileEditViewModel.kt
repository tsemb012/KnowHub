package com.example.droidsoftthird

import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(private val useCase: ProfileUseCase): ProfileSubmitViewModel(useCase) {

    fun fetchUserDetail() {
        val job = viewModelScope.launch {
            runCatching { useCase.fetchUserDetail() }
                .onSuccess { initializeUiModel(it) }
                .onFailure { loadState.value = LoadState.Error(it) }
        }
        loadState.value = LoadState.Loading(job)
        job.start()
    }

    private fun initializeUiModel(detail: UserDetail) {

        rawUserDetail.value = detail
        bindingUserName.value = detail.userName
        bindingComment.value = detail.comment
        temporalUserImage.value = mapOf(REF_FOR_INITIALIZE to detail.userImage)
        loadState.value = LoadState.Loaded(detail)
        postGender(UserDetail.Gender.valueOf(detail.gender.uppercase(Locale.ROOT)))
        postBirthday(detail.birthday)
        postArea(detail.area)
    }

    fun submitEditedUserProfile() = submitProfile(SubmitType.EDIT)

}
