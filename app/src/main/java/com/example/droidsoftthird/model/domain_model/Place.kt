package com.example.droidsoftthird.model.domain_model

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.PlusCode
import java.lang.Math.round
import kotlin.math.roundToInt

data class Place (
        val placeId: String,
        val name: String,
        val types: List<String>,
        val location: Location,
        val viewPort: ViewPort,
        val formattedAddress: String?,
        val plusCode: PlusCode,
        val photos: List<LocationPhoto>?
)

data class LocationPhoto (
        val height: Int,
        val width: Int,
        val photoReference: String,
        val htmlAttributions: List<String>,
)

data class YolpSimplePlace (
        val id: String,
        val name: String,
        val category: String?,
        val location: Location
) {
        fun calculateKiloMeter(centerPoint: LatLng): Double {
                val distance = FloatArray(3)
                android.location.Location.distanceBetween(centerPoint.latitude, centerPoint.longitude, location.lat, location.lng, distance)
                return round(distance[0] / 1000 * 10) / 10.0
        }
}

enum class Category {
        CAFE,
        PARK,
        LIBRARY,
}