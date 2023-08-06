package com.tsemb.droidsoftthird.model.infra_model.json.response

import com.tsemb.droidsoftthird.model.domain_model.ViewPort
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json

data class GetViewPortJson (
    @Json(name = "northeast")
        val northEast: NorthEast?,
    @Json(name = "southwest")
        val southWest: SouthWest?
) {
    data class NorthEast (
            val lat: Double? = null,
            val lng: Double? = null
    )
    data class SouthWest (
            val lat: Double? = null,
            val lng: Double? = null
    )
    fun toEntity(): ViewPort = ViewPort(
            northEast = LatLng(northEast?.lat ?: 0.0, northEast?.lng ?: 0.0),
            southWest = LatLng(southWest?.lat ?: 0.0, southWest?.lng ?: 0.0)
    )
}