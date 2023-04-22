package com.example.droidsoftthird

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.domain_model.initializedUserDetail
import com.example.droidsoftthird.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val useCase: ProfileUseCase): ViewModel() {

    //TODO 基本的に個人情報はRailsAPIにしまう。ユーザー名とユーザー画像だけはFirebaseとRailsAPIどちらにも入れる。

    private val _userDetail = mutableStateOf(initializedUserDetail)
    val userDetail: MutableState<UserDetail> = _userDetail
    val downloadUrl1: MutableState<String> = mutableStateOf("")

    init { fetchUserDetail() }

    private fun fetchUserDetail() {
        viewModelScope.launch {
            
            try {
                val userDetail = async { useCase.fetchUserDetail() }
                _userDetail.value = userDetail.await()
                downloadUrl1.value = useCase.fetchUserImage(_userDetail.value.userImage)
            } catch (e: Exception) {
                Log.d("tsemb012", "${e.message}")
            }
        }
    }
}
