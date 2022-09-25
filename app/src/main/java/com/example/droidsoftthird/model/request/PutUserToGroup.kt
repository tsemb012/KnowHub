package com.example.droidsoftthird.model.request

import com.squareup.moshi.Json

data class PutUserToGroup (
        @Json(name = "user_id")
        val userId: String,
)