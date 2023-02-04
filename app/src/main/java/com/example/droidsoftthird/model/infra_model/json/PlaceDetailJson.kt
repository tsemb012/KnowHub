package com.example.droidsoftthird.model.infra_model.json

import com.example.droidsoftthird.model.domain_model.*
import com.google.android.libraries.places.api.model.PlusCode
import com.squareup.moshi.Json

data class PlaceDetailJson(
        @Json(name = "place_id")
        val placeId: String,
        val name: String,
        val types: List<String>,
        val location: Location,
        @Json(name = "viewport")
        val viewPort: ViewPortJson,
        @Json(name = "formatted_address")
        val formattedAddress: String?,
        @Json(name = "plus_code")
        val plusCode: PlusCode,
        val photos: List<LocationPhoto>?,
        val color: String?,
        val url: String?,
        @Json(name = "address_components")
        val addressComponents: List<AddressComponentJson>,
) {
    data class AddressComponentJson(
            @Json(name = "long_name")
            val longName: String,
            @Json(name = "short_name")
            val shortName: String,
            val types: List<String>
    ) {
        fun toEntity() = PlaceDetail.AddressComponent(
                longName = longName,
                shortName = shortName,
                types = types,
        )
    }

    fun toEntity(): PlaceDetail = PlaceDetail(
            placeId = placeId,
            name = name,
            types = types,
            location = location,
            viewPort = viewPort.toEntity(),
            formattedAddress = formattedAddress,
            plusCode = plusCode,
            photos = photos,
            color = color,
            url = url,
            addressComponents = addressComponents.map { it.toEntity() },
    )
}