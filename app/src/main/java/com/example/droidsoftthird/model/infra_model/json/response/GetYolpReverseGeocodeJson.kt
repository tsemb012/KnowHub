package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.YolpReverseGeocode

data class GetYolpReverseGeocodeJson (
    val address: String,
    val lat: Double,
    val lng: Double,
) {
    fun toEntity() = YolpReverseGeocode(
        address = address,
        latitude = lat,
        longitude = lng,
    )
}