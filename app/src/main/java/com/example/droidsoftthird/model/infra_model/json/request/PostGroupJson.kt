package com.example.droidsoftthird.model.infra_model.json.request

import com.squareup.moshi.Json

data class PostGroupJson(
        @Json(name = "host_id")
        val hostId: String,
        val name: String,
        val introduction: String,//TODO descriptionに変更した方が良い。
        @Json(name = "group_type")
        val groupType: String,
        val prefecture_code: Int,
        val city_code: Int,
        @Json(name = "is_online")
        val isOnline: Boolean,
        @Json(name = "facility_environment")
        val facilityEnvironment: String,
        val style: String,
        @Json(name = "frequency_basis")
        val frequencyBasis: String,
        @Json(name = "frequency_times")
        val frequencyTimes: Int,
        @Json(name = "max_age")
        val maxAge: Int,
        @Json(name = "min_age")
        val minAge: Int,
        @Json(name = "max_number")
        val maxNumber: Int,
        @Json(name = "is_same_sexuality")
        val isSameSexuality: Boolean,
        @Json(name = "image_url")
        val imageUrl: String,
)
