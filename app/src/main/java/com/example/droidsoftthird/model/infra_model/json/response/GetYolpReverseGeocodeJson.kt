package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.YolpSinglePlace

data class GetYolpReverseGeocodeJson (
    val address: String,
    val lat: Double,
    val lng: Double,
) {
    fun toEntity() = YolpSinglePlace.ReverseGeocode(
        address = address,
        lat = lat,
        lng = lng,
    )
}