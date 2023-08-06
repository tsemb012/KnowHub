package com.tsemb.droidsoftthird.model.infra_model.json.response

import com.squareup.moshi.Json
import com.tsemb.droidsoftthird.model.domain_model.AddressComponent
import com.tsemb.droidsoftthird.model.domain_model.Location
import com.tsemb.droidsoftthird.model.domain_model.LocationPhoto
import com.tsemb.droidsoftthird.model.domain_model.YolpSinglePlace

data class GetPlaceDetailJson(
    @Json(name = "place_id")
        val placeId: String,
    val name: String,
    val types: List<String>,
    val geometry: GetPlaceJson.GeometryJson,
    @Json(name = "formatted_address")
        val formattedAddress: String,
    @Json(name = "plus_code")
        val plusCode: GetPlaceJson.PlusCodeJson,
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
}

data class GetYolpDetailPlaceJson(
        val id: String,
        val name: String,
        val yomi: String?,
        val category: String?,
        val tel: String?,
        val url: String?,
        val lat: Double,
        val lng: Double,
        val address: String?,
) {

    fun toEntity(): YolpSinglePlace.DetailPlace = YolpSinglePlace.DetailPlace(
            id = id,
            name = name,
            yomi = yomi,
            category = category,
            tel = tel,
            url = url,
            location = Location(lat, lng),
            address = address ?: "",
    )
}