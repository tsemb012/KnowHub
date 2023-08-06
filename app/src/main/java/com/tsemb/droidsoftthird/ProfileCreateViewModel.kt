package com.tsemb.droidsoftthird

import com.tsemb.droidsoftthird.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileCreateViewModel @Inject constructor(useCase: ProfileUseCase): ProfileSubmitViewModel(useCase) {

    fun submitNewUserProfile() = submitProfile(SubmitType.CREATE)

}
