package com.example.droidsoftthird.model.request

import com.squareup.moshi.Json

data class PostGroup(
    @Json(name = "host_id")
    val hostId: String,
    val name: String,
    val introduction: String,//TODO descriptionに変更した方が良い。
    val type: String,
    val prefecture: String,
    val city: String,
    @Json(name = "facility_environment")
    val facilityEnvironment: String,
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
    @Json(name = "min_number")
    val minNumber: Int,
    @Json(name = "is_same_sexuality")
    val isSameSexuality: Boolean,
    @Json(name = "image_url")
    val imageUrl: String,
)
