package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.Location
import com.example.droidsoftthird.model.domain_model.LocationPhoto
import com.example.droidsoftthird.model.domain_model.Place
import com.example.droidsoftthird.model.domain_model.YolpSimplePlace
import com.google.android.libraries.places.api.model.PlusCode
import com.squareup.moshi.Json

data class GetPlaceJson(
    @Json(name = "place_id")
    val placeId: String,
    val name: String,
    val geometry: GeometryJson,
    val types: List<String>,
    @Json(name = "plus_code")
        val plusCode: PlusCodeJson,
    @Json(name = "formatted_address")
        val formattedAddress: String?,
    @Json(name = "photos")
        val photos: List<PhotoJson>?,
) {
    data class GeometryJson(
        val location: Location,
        val viewport: GetViewPortJson,
    )
    data class PhotoJson(
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
    data class PlusCodeJson(
            @Json(name = "compound_code")
            val compoundCode: String,
            @Json(name = "global_code")
            val globalCode: String,
    ) {
        fun toEntity() = PlusCode.builder().setCompoundCode(compoundCode).setGlobalCode(globalCode).build()
    }

    fun toEntity(): Place = Place(
            placeId = placeId,
            name = name,
            types = types,
            location = geometry.location,
            viewPort = geometry.viewport.toEntity(),
            formattedAddress = formattedAddress,
            plusCode = plusCode.toEntity(),
            photos = photos?.map { it.toEntity() },
    )
}

data class GetYolpSimplePlaceJson(
        val id: String,
        val name: String,
        val category: String,
        val lat: Double,
        val lng: Double,
) {
    fun toEntity() = YolpSimplePlace(
            id = id,
            name = name,
            category = category,
            location = Location(lat, lng),
    )
}
