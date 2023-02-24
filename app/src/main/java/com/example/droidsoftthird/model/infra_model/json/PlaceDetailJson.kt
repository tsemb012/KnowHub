package com.example.droidsoftthird.model.infra_model.json

import com.example.droidsoftthird.model.domain_model.*
import com.google.android.libraries.places.api.model.PlusCode
import com.squareup.moshi.Json

data class PlaceDetailJson(
        @Json(name = "place_id")
        val placeId: String,
        val name: String,
        val types: List<String>,
        val geometry: PlaceJson.GeometryJson,
        @Json(name = "formatted_address")
        val formattedAddress: String,
        @Json(name = "plus_code")
        val plusCode: PlaceJson.PlusCodeJson,
        val photos: List<LocationPhotoJson>?,
        @Json(name = "icon_background_color")
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
        fun toEntity() = AddressComponent(
                longName = longName,
                shortName = shortName,
                types = types,
        )
    }

    data class LocationPhotoJson(
            val height: Int,
            val width: Int,
            @Json(name = "photo_reference")
            val photoReference: String,
            @Json(name = "html_attributions")
            val htmlAttributions: List<String>,
    ) {
        fun toEntity() = LocationPhoto(
                height = height,
                width = width,
                photoReference = photoReference,
                htmlAttributions = htmlAttributions,
        )
    }

    fun toEntity(): PlaceDetail = PlaceDetail(
            placeId = placeId,
            name = name,
            types = types,
            location = geometry.location,
            viewPort = geometry.viewport.toEntity(),
            formattedAddress = formattedAddress,
            plusCode = plusCode.toEntity(),
            photos = photos?.map { it.toEntity() },
            color = color,
            url = url,
            addressComponents = addressComponents.map { it.toEntity() },
    )
}