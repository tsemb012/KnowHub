package com.example.droidsoftthird.model.response

import com.example.droidsoftthird.model.domain_model.Area
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.squareup.moshi.Json

data class GetUserDetail (
    @Json(name = "id")
    val userId: String,
    @Json(name = "user_name")
    val userName: String,
    @Json(name = "user_image")
    val userImage: String,
    @Json(name = "back_ground_image")
    val backgroundImage: String,
    val comment:String,
    val gender: String,
    val age: Int,
    val area: Area,
    val groups: List<GetGroup>,
    //val schedules: List<GetShcedule> TODO スケジュール実装後に追加
) {
    fun toEntity() =
        UserDetail(
                userId = userId,
                userName = userName,
                userImage = userImage,
                backgroundImage = backgroundImage,
                comment = comment,
                gender = gender,
                age = age,
                area = area,
                groups = groups.map { it.toEntity() },
        )
}
