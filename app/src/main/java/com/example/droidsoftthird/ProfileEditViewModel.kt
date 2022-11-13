package com.example.droidsoftthird

import androidx.compose.ui.text.toUpperCase
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.fire_model.LoadState
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

    private fun initializeUiModel(it: UserDetail) {

        rawUserDetail.value = it
        temporalUserImage.value = it.userImage.toUri()
        bindingUserName.value = it.userName
        bindingComment.value = it.comment
        loadState.value = LoadState.Loaded(it)
        postGender(UserDetail.Gender.valueOf(it.gender.uppercase(Locale.ROOT)))
        postAge(it.age)
        postArea(it.area)
        //storeTemporalUserImage()
    }

    fun submitEditedUserProfile() = submitProfile(SubmitType.EDIT)

}
