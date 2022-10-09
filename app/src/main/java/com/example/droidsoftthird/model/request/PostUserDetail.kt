package com.example.droidsoftthird.model.request

import com.example.droidsoftthird.model.rails_model.Area
import com.squareup.moshi.Json

class PostUserDetail(
        @Json(name = "id")
        val userId: String,
        @Json(name = "user_name")
        val userName: String? = null,
        @Json(name = "user_image")
        val userImage: String,
        @Json(name = "back_ground_image")
        val groupImage: String,
        val comment:String,
        val gender: String,
        val age: Int,
        val area: Area,
)
