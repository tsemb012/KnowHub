package com.example.droidsoftthird.usecase

import android.net.Uri
import androidx.paging.PagingData
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.EditedGroup
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GroupUseCase @Inject constructor(private val repository: BaseRepositoryImpl, ) {

    suspend fun createGroup(group: EditedGroup) = repository.createGroup(group)
    suspend fun fetchGroups(page: Int) = repository.fetchGroups(page)
    suspend fun fetchGroupDetail(groupId: String) = repository.fetchGroupDetail(groupId)
    suspend fun userJoinGroup(groupId: String) = repository.userJoinGroup(groupId)
    suspend fun fetchJoinedGroups() = repository.fetchJoinedGroups()
    suspend fun uploadPhoto(value: Uri) = repository.uploadPhoto(value)
    suspend fun fetchCountByArea() = repository.fetchGroupCountByArea()
    suspend fun fetchGroupsByArea(code: Int, category: String): Flow<PagingData<ApiGroup>> = if (category == "prefecture") {
        fetchGroupsByPrefecture(code)
    } else {
        fetchGroupsByCity(code)
    }

    private suspend fun fetchGroupsByPrefecture(code: Int) = repository.fetchGroups(code)
    private suspend fun fetchGroupsByCity(code: Int) = repository.fetchGroups(code)

}