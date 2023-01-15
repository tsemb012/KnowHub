package com.example.droidsoftthird.model.json

import com.example.droidsoftthird.model.domain_model.Location
import com.example.droidsoftthird.model.domain_model.Place
import com.squareup.moshi.Json

data class PlaceJson(
        @Json(name = "place_id")
        val placeId: String,
        val name: String,
        val geometry: GeometryJson,
        val types: List<TypeJson>,
        @Json(name = "plus_code")
        val plusCode: PlusCodeJson,
        @Json(name = "formatted_address")
        val formattedAddress: String,
        @Json(name = "photos")
        val photos: List<PhotoJson>,
) {
    data class GeometryJson(
            val location: Location,
            val viewport: ViewPortJson,
    )
    data class PhotoJson(
            val height: Int,
            val width: Int,
            @Json(name = "photo_reference")
            val photoReference: String,
            @Json(name = "html_attributions")
            val htmlAttributions: List<String>,
    )
    data class PlusCodeJson(
            @Json(name = "compound_code")
            val compoundCode: String,
            @Json(name = "global_code")
            val globalCode: String,
    )
    data class TypeJson(
            @Json(name = "point_of_interest")
            val pointOfInterest: String,
            val establishment: String,
    )
    fun toEntity(): Place = Place(
            placeId = placeId,
            name = name,
            type = types[0].pointOfInterest,
            location = geometry.location,
            viewPort = geometry.viewport.toEntity(),
            formattedAddress = formattedAddress,
            //photos = photos,
    )
}
