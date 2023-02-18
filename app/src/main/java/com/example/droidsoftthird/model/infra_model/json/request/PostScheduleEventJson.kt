package com.example.droidsoftthird.model.infra_model.json.request

data class PostScheduleEventJson(
        val hostId: String,
        val name: String,
        val comment: String,
        val date: String,
        val start: String,
        val end: String,
        val place: EditedPlaceJson,
        val groupId: String
)

data class EditedPlaceJson(
        val name: String,
        val address: String,
        val latitude: Double,
        val longitude: Double,
        val placeId: String,
        val type: String,
        val globalCode: String?,
        val compoundCode: String?,
        val url: String?,
        val memo: String?
)
