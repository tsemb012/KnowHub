package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.GroupLocation
import com.squareup.moshi.Json

data class GetGroupLocationJson (
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    @Json(name = "group_count")
    val groupCount : Int
    //TODO 市区町村の情報を追加する
) {
    fun toEntity() = GroupLocation(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        groupCount = groupCount
    )
}