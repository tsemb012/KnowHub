package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.squareup.moshi.Json

data class GetGroup(//TODO 複雑なものをGroupDetailに責務を渡して、ただのGroupはシンプルにするべき。
        val id: String,
        @Json(name = "host_id")
        val hostId: String,
        val name: String,
        val introduction: String,//TODO descriptionに変更した方が良い。
        @Json(name = "group_type")
        val groupType: String,
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
) {
    fun toEntity() =
        ApiGroup(
                groupId = id,
                hostUserId = hostId,
                storageRef = imageUrl,
                groupName = name,
                groupIntroduction = introduction,
                groupType = groupType,
                prefecture = prefecture,
                city = city,
                facilityEnvironment = facilityEnvironment,
                basis = frequencyBasis,
                frequency = frequencyTimes,
                minAge = minAge,
                maxAge = maxAge,
                minNumberPerson = minNumber,
                maxNumberPerson = maxNumber,
                isChecked = isSameSexuality,
        )
}