package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.infra_model.json.request.PostUserDetailJson

data class UserDetail (
        val userId: String,
        val userName: String,
        val userImage: String,
        val comment:String,
        val gender: String,
        val age: Int,
        val area: Area,
        val groups: List<ApiGroup>,
        val events: List<ItemEvent>,
) {
    enum class Gender { MALE, FEMALE, NO_ANSWER }
    fun toJson() =
        PostUserDetailJson(
                userId = userId,
                userName = userName,
                userImage = userImage,
                comment = comment,
                gender = gender,
                age = age,
                prefectureCode = area.prefecture?.prefectureCode ?: -1,
                cityCode = area.city?.cityCode ?: -1,
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
                area = Area(null, null),
                groups = listOf(),
                events = listOf(),
        )


