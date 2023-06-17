package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.FacilityEnvironment
import com.example.droidsoftthird.model.domain_model.FrequencyBasis
import com.example.droidsoftthird.model.domain_model.GroupType
import com.example.droidsoftthird.model.domain_model.Style
import com.squareup.moshi.Json

data class GetGroupJson(//TODO 複雑なものをGroupDetailに責務を渡して、ただのGroupはシンプルにするべき。
        val id: String,
        @Json(name = "host_id")
        val hostId: String,
        val name: String,
        val introduction: String,//TODO descriptionに変更した方が良い。
        @Json(name = "group_type")
        val groupType: String,
        val prefecture: String? = null,
        @Json(name = "is_online")
        val isOnline: Boolean,
        val city: String? = null,
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
) {
    fun toEntity() =
        ApiGroup(
                groupId = id,
                hostUserId = hostId,
                storageRef = imageUrl,
                groupName = name,
                groupIntroduction = introduction,
                groupType = GroupType.valueOf(groupType.uppercase()),
                prefecture = prefecture,
                city = city,
                isOnline = isOnline,
                facilityEnvironment = FacilityEnvironment.valueOf(facilityEnvironment.uppercase()),
                style = Style.valueOf(style.uppercase()),
                basis = FrequencyBasis.valueOf(frequencyBasis.uppercase()),
                frequency = frequencyTimes,
                minAge = minAge,
                maxAge = maxAge,
                maxNumberPerson = maxNumber,
                isChecked = isSameSexuality,
        )
}