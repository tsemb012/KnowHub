package com.example.droidsoftthird

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.domain_model.initializedUserDetail
import com.example.droidsoftthird.model.domain_model.fire_model.UserProfile
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.google.firebase.auth.ktx.userProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileCreateViewModel @Inject constructor(useCase: ProfileUseCase): ProfileSubmitViewModel(useCase) {

    fun submitNewUserProfile() = submitProfile(SubmitType.CREATE)

}
