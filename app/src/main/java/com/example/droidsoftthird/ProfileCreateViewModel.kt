package com.example.droidsoftthird

import com.example.droidsoftthird.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileCreateViewModel @Inject constructor(useCase: ProfileUseCase): ProfileSubmitViewModel(useCase) {

    fun submitNewUserProfile() = submitProfile(SubmitType.CREATE)

}
