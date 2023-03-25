package com.example.droidsoftthird

import android.util.Log
import androidx.lifecycle.*
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val useCase: ProfileUseCase): ViewModel() {

    //TODO 基本的に個人情報はRailsAPIにしまう。ユーザー名とユーザー画像だけはFirebaseとRailsAPIどちらにも入れる。

    private val _userDetail = MutableLiveData<UserDetail?>()
    val userDetail: LiveData<UserDetail?>
        get() = _userDetail
    val userAge: LiveData<String> = Transformations.map(userDetail) { it?.age?.toString() }
    val residentialArea: LiveData<String> = Transformations.map(userDetail) { it?.area?.prefecture?.name + ", " + it?.area?.city?.name }

    fun fetchUserDetail() {
        viewModelScope.launch {
            kotlin.runCatching { useCase.fetchUserDetail() }
                .onSuccess {
                    _userDetail.value = it
                }
                .onFailure {
                    Log.d("tsemb012", "${it.message}")
                }
        }
    }

}


