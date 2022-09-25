package com.example.droidsoftthird.repository

import com.example.droidsoftthird.model.User
import com.example.droidsoftthird.model.json.SignUpJson
import com.example.droidsoftthird.model.rails_model.ApiGroup
import com.example.droidsoftthird.model.rails_model.ApiGroupDetail

interface RailsApiRepository {

    suspend fun postNewUser(singup: SignUpJson): User?
    suspend fun createGroup(group: ApiGroup): String?
    suspend fun fetchGroupDetail(groupId: String) : ApiGroupDetail
    suspend fun fetchGroups(nextPage: Int) : List<ApiGroup>
    suspend fun userJoinGroup(groupId: String): String?


}