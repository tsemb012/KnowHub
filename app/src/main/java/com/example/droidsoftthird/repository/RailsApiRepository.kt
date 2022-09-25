package com.example.droidsoftthird.repository

import com.example.droidsoftthird.model.User
import com.example.droidsoftthird.model.fire_model.Group
import com.example.droidsoftthird.model.json.SignUpJson
import com.example.droidsoftthird.model.rails_model.ApiGroup

interface RailsApiRepository {

    suspend fun postNewUser(singup: SignUpJson): User?

    suspend fun createGroup(group: ApiGroup): String?
    suspend fun fetchGroup(groupId: String) : ApiGroup
    suspend fun fetchGroups(nextPage: Int) : List<ApiGroup>

}