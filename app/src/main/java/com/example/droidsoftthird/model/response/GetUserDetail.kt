package com.example.droidsoftthird.model.response

import com.example.droidsoftthird.model.rails_model.Area
import com.squareup.moshi.Json

data class GetUserDetail (
        @Json(name = "id")
        val userId: String,
        @Json(name = "user_name")
        val userName: String? = null,
        @Json(name = "user_image")
        val userImage: String,
        @Json(name = "back_ground_image")
        val backGroundImage: String,
        val comment:String,
        val gender: String,
        val age: Int,
        val area: Area,
        val groups: List<GetGroup>,
        //val schedules: List<GetShcedule> TODO スケジュール実装後に追加
)
