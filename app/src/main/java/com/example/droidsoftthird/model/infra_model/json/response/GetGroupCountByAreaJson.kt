package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.AreaCategory
import com.example.droidsoftthird.model.domain_model.GroupCountByArea
import com.squareup.moshi.Json

data class GetGroupCountByAreaJson (
    val category : String,
    val code: String,
    @Json(name = "prefecture_name")
    val prefectureName: String,
    @Json(name = "city_name")
    val cityName: String?,
    val latitude: Double,
    val longitude: Double,
    @Json(name = "group_count")
    val groupCount : Int
) {
    fun toEntity() = GroupCountByArea(
        category = AreaCategory.valueOf(category.uppercase()),
        code = code,
        prefectureName = prefectureName,
        cityName = cityName,
        latitude = latitude,
        longitude = longitude,
        groupCount = groupCount
    )
}