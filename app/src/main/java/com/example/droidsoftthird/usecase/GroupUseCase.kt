package com.example.droidsoftthird.usecase

import android.net.Uri
import androidx.paging.map
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.EditedGroup
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GroupUseCase @Inject constructor(private val repository: BaseRepositoryImpl) {

    suspend fun createGroup(group: EditedGroup) = repository.createGroup(group)
    suspend fun fetchGroups(groupFilterCondition: ApiGroup.FilterCondition) = repository.fetchGroups(groupFilterCondition).map {
        it.map { group -> group.copy(storageRef = fetchGroupImage(group.storageRef)) } //TODO 将来的にS3に置き換えるか、サーバーサイドでURLを取得するようにする
    }
    suspend fun fetchGroupDetail(groupId: String) = repository.fetchGroupDetail(groupId)
    suspend fun userJoinGroup(groupId: String) = repository.userJoinGroup(groupId)
    suspend fun userLeaveGroup(groupId: String) = repository.userLeaveGroup(groupId)
    suspend fun fetchJoinedGroups() = repository.fetchJoinedGroups().map { group ->
        group.copy(storageRef = fetchGroupImage(group.storageRef)) //TODO グループの画像処理を修正できたらUserUseCaseに持っていく。
    }
    suspend fun uploadPhoto(value: Uri) = repository.uploadPhoto(value)
    suspend fun fetchCountByArea() = repository.fetchGroupCountByArea()
    private suspend fun fetchGroupImage(imagePath: String) = repository.fetchStorageImage(imagePath)

    suspend fun fetchPrefectureList() = repository.loadPrefectureCsv()
    suspend fun fetchCityList() = repository.loadCityCsv()
}
