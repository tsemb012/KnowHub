package com.example.droidsoftthird.repository

import android.net.Uri
import com.example.droidsoftthird.Result
import com.example.droidsoftthird.model.domain_model.fire_model.FireGroup
import com.example.droidsoftthird.model.domain_model.fire_model.RawScheduleEvent
import com.example.droidsoftthird.model.domain_model.fire_model.FireUserProfile
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.StorageReference

interface FirebaseRepository {

    suspend fun signUpAndFetchToken(email: String, password: String): Result<String>

    suspend fun singIn(email: String, password: String): Result<String>

    suspend fun getGroups(query: String): Result<List<FireGroup>>

    suspend fun getGroup(groupId: String): Result<FireGroup?>

    suspend fun uploadPhoto(uri: Uri): Result<StorageReference>

    suspend fun getUserProfile(): Result<FireUserProfile?>

    suspend fun createUserProfile(userProfile: FireUserProfile): Result<Int>

    suspend fun updateAuthProfile(authProfileUpdates: UserProfileChangeRequest): Result<Int>

    suspend fun getSchedules(query: String): Result<List<RawScheduleEvent>>

    suspend fun getUserId(): String

    suspend fun fetchStorageImage(userImagePath: String): String

}