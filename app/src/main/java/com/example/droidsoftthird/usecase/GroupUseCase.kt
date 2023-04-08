package com.example.droidsoftthird.usecase

import android.net.Uri
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import javax.inject.Inject

class GroupUseCase @Inject constructor(private val repository: BaseRepositoryImpl) {

    suspend fun createGroup(group: ApiGroup) = repository.createGroup(group)
    suspend fun fetchGroups(page: Int) = repository.fetchGroups(page)
    suspend fun fetchGroupDetail(groupId: String) = repository.fetchGroupDetail(groupId)
    suspend fun userJoinGroup(groupId: String) = repository.userJoinGroup(groupId)
    suspend fun fetchJoinedGroups() = repository.fetchJoinedGroups()
    suspend fun uploadPhoto(value: Uri) = repository.uploadPhoto(value)
    //suspend fun fetchGroupLocations() = repository.fetchGroupLocations()

}