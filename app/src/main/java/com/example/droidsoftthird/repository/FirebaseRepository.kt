package com.example.droidsoftthird.repository

import android.net.Uri
import com.example.droidsoftthird.Result
import com.example.droidsoftthird.model.fire_model.Group
import com.example.droidsoftthird.model.fire_model.RawScheduleEvent
import com.example.droidsoftthird.model.fire_model.UserProfile
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.StorageReference

interface FirebaseRepository {

    suspend fun signUp(email: String, password: String): Result<String>

    suspend fun getGroups(query: String): Result<List<Group>>

    suspend fun getGroup(groupId: String): Result<Group?>

    suspend fun uploadPhoto(uri: Uri): Result<StorageReference>

    suspend fun getUserProfile(): Result<UserProfile?>

    suspend fun createUserProfile(userProfile: UserProfile): Result<Int>

    suspend fun updateAuthProfile(authProfileUpdates: UserProfileChangeRequest): Result<Int>

    suspend fun userJoinGroup(groupId: String): Result<Int>

    suspend fun getSchedules(query: String): Result<List<RawScheduleEvent>>

}