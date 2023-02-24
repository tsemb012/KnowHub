package com.example.droidsoftthird.model.infra_model.json.request

import com.squareup.moshi.Json

data class PostScheduleEventJson(
        @Json(name = "host_id")
        val hostId: String,
        val name: String,
        val comment: String,
        val date: String,
        @Json(name = "start_time")
        val startTime: String,
        @Json(name = "end_time")
        val endTime: String,
        val place: EditedPlaceJson,
        @Json(name = "group_id")
        val groupId: String
)

data class EditedPlaceJson(
        val name: String,
        val address: String,
        val latitude: Double,
        val longitude: Double,
        @Json(name = "place_id")
        val placeId: String,
        @Json(name = "place_type")
        val placeType: String,
        @Json(name = "global_code")
        val globalCode: String?,
        @Json(name = "compound_code")
        val compoundCode: String?,
        val url: String?,
        val memo: String?
)
