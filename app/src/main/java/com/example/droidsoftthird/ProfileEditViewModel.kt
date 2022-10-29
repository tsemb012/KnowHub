package com.example.droidsoftthird

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.fire_model.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(private val useCase: ProfileUseCase): ProfileSubmitViewModel(useCase) {

    fun fetchUserDetail() {
        val job = viewModelScope.launch {
            runCatching { useCase.fetchUserDetail() }
                .onSuccess {
                    rawUserDetail.value = it
                    temporalUserImage.value = it.userImage.toUri()
                    bindingUserName.value = it.userName
                    bindingComment.value = it.comment
                    loadState.value = LoadState.Loaded(it)
                }
                .onFailure { loadState.value = LoadState.Error(it) }
        }
        loadState.value = LoadState.Loading(job)
        job.start()
    }

    fun submitEditedUserProfile() = submitProfile(SubmitType.EDIT)

}
