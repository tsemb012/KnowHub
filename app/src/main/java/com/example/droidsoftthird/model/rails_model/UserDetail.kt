package com.example.droidsoftthird.model.rails_model

import com.example.droidsoftthird.model.request.PostUserDetail

data class UserDetail (
        val userId: String,
        val userName: String,
        val userImage: String,
        val backGroundImage: String,
        val comment:String,
        val gender: String,
        val age: Int,
        val area: Area,
        val groups: List<ApiGroup>,
) {
    fun toJson() =
        PostUserDetail(
                userId = userId,
                userName = userName,
                userImage = userImage,
                backGroundImage = backGroundImage,
                comment = comment,
                gender = gender,
                age = age,
                area = area,
        )
}
