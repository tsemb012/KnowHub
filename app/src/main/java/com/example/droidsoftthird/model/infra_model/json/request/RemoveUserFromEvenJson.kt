package com.example.droidsoftthird.model.infra_model.json.request

import com.squareup.moshi.Json

data class RemoveUserFromEventJson (
    @Json(name = "user_id")
    val userId: String,
)
