package com.tsemb.droidsoftthird.model.infra_model.json.request

import com.squareup.moshi.Json

data class PutUserToEventJson(
        @Json(name = "user_id")
        val userId: String,
)