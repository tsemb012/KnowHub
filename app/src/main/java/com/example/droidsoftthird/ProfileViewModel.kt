package com.example.droidsoftthird

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.domain_model.initializedUserDetail
import com.example.droidsoftthird.usecase.ProfileUseCase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val useCase: ProfileUseCase): ViewModel() {

    //TODO 基本的に個人情報はRailsAPIにしまう。ユーザー名とユーザー画像だけはFirebaseとRailsAPIどちらにも入れる。

    private val _userDetail = mutableStateOf(initializedUserDetail)
    val userDetail: MutableState<UserDetail> = _userDetail
    val downloadUrl1: MutableState<String> = mutableStateOf("")
    val userAge: String = ""//Transformations.map(userDetail) { it?.age?.toString() }
    val residentialArea: String = ""//Transformations.map(userDetail) { it?.area?.prefecture?.name + ", " + it?.area?.city?.name }

    init { fetchUserDetail() }

    fun fetchUserDetail() {
        viewModelScope.launch {
            kotlin.runCatching { useCase.fetchUserDetail() }
                .onSuccess {
                    _userDetail.value = it
                    val imageReference = FirebaseStorage.getInstance().getReference(userDetail.value.userImage)
                    imageReference.getDownloadUrlOrNull()
                    //imageUrl?.let { url -> downloadUrl.value = imageUrl }
                    _userDetail.value = it
                }
                .onFailure {
                    Log.d("tsemb012", "${it.message}")
                }
        }
    }

    suspend fun StorageReference.getDownloadUrlOrNull() {
        try {
            downloadUrl.addOnSuccessListener {
                downloadUrl1.value = it.toString()
            }
        } catch (e: Exception) {
            throw e
        }
    }

}
