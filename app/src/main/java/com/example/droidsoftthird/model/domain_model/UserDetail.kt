package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.request.PostUserDetail

data class UserDetail (
        val userId: String,
        val userName: String,
        val userImage: String,
        val comment:String,
        val gender: String,
        val age: Int,
        val area: Area?,
        val groups: List<ApiGroup>,
) {
    enum class Gender { MALE, FEMALE, NO_ANSWER }
    fun toJson() =
        PostUserDetail(
                userId = userId,
                userName = userName,
                userImage = userImage,
                comment = comment,
                gender = gender,
                age = age,
                area = area ?: throw IllegalArgumentException("area is null"),
        )
}

internal val initializedUserDetail get() =
        UserDetail(
                userId = "",
                userName = "",
                userImage = "",
                comment = "",
                gender = "",
                age = -1,
                area = null,
                groups = listOf()
        )


