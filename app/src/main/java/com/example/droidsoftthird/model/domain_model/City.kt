package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.infra_model.json.request.CityJson
import com.squareup.moshi.Json

data class City (
        @Json(name = "city_code")
        val cityCode: Int,
        val name: String,
        val spell: String,
        val latitude: Double,
        val longitude: Double,
) {
    fun toJson() =
        CityJson(
                cityCode = cityCode,
                name = name,
                spell = spell,
                latitude = latitude,
                longitude = longitude
        )
}
