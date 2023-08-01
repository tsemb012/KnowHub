package com.example.droidsoftthird.usecase

import android.net.Uri
import com.example.droidsoftthird.Result
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val repository: BaseRepositoryImpl) {
    suspend fun fetchUserDetail() = repository.fetchUser()
    suspend fun fetchUserId() = repository.getUserId()
    suspend fun createUserDetail(userDetail: UserDetail) = repository.createUser(userDetail)
    suspend fun updateUserDetail(userDetail: UserDetail) = repository.updateUserDetail(userDetail)
    suspend fun uploadImage(image: Uri):StorageReference = repository.uploadPhoto(image).let {
        when (it) {
            is Result.Success -> it.data
            is Result.Failure -> throw IllegalStateException("Failed to upload image.")
        }
    }
    suspend fun updateAuthProfile(authUserDetail: UserProfileChangeRequest) = repository.updateAuthProfile(authUserDetail)

    suspend fun fetchUserJoinedGroupIds() = repository.fetchUserJoinedGroupIds()

    suspend fun fetchUserJoinedSimpleGroups() = repository.fetchUserJoinedSimpleGroups()

    suspend fun fetchUserImage(imagePath: String): String = repository.fetchStorageImage(imagePath)


}
