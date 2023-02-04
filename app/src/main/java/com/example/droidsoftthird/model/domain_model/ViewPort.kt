package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.infra_model.json.ViewPortJson
import com.google.android.gms.maps.model.LatLng

data class ViewPort(
    val northEast: LatLng?,
    val southWest: LatLng?
) {
    fun toJson(): ViewPortJson = ViewPortJson(
            northEast = ViewPortJson.NorthEast(
                    lat = northEast?.latitude,
                    lng = northEast?.longitude
            ),
            southWest = ViewPortJson.SouthWest(
                    lat = southWest?.latitude,
                    lng = southWest?.longitude
            )
    )
}
