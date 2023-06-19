package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.SimpleUser
import com.squareup.moshi.Json

data class GetSimpleUserJson (
    @Json(name = "user_id")
    val userId: String,
    @Json(name = "user_name")
    val userName: String,
    @Json(name = "user_image")
    val userImage: String,
) {
    fun toEntity() =
        SimpleUser(
            userId = userId,
            userName = userName,
            userImage = userImage,
        )
}