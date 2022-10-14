package com.example.droidsoftthird.repository

import com.example.droidsoftthird.model.User
import com.example.droidsoftthird.model.json.SignUpJson
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.ApiGroupDetail
import com.example.droidsoftthird.model.domain_model.UserDetail

interface RailsApiRepository {

    suspend fun postNewUser(singup: SignUpJson): User?
    suspend fun createGroup(group: ApiGroup): String?
    suspend fun fetchGroupDetail(groupId: String) : ApiGroupDetail
    suspend fun fetchGroups(page: Int) : List<ApiGroup>
    suspend fun fetchJoinedGroups() : List<ApiGroup>

    suspend fun userJoinGroup(groupId: String): String?
    suspend fun fetchUser(): UserDetail
}