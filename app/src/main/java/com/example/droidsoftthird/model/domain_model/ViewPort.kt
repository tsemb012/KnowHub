package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.infra_model.json.response.GetViewPortJson
import com.google.android.gms.maps.model.LatLng

data class ViewPort(
    val northEast: LatLng?,
    val southWest: LatLng?
) {
    fun toJson(): GetViewPortJson = GetViewPortJson(
            northEast = GetViewPortJson.NorthEast(
                    lat = northEast?.latitude,
                    lng = northEast?.longitude
            ),
            southWest = GetViewPortJson.SouthWest(
                    lat = southWest?.latitude,
                    lng = southWest?.longitude
            )
    )
}
