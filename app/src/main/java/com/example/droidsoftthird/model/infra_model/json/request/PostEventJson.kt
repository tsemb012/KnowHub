package com.example.droidsoftthird.model.infra_model.json.request

import com.example.droidsoftthird.model.domain_model.EditedPlace
import com.example.droidsoftthird.model.domain_model.Location
import com.google.android.libraries.places.api.model.PlusCode
import com.squareup.moshi.Json

data class PostEventJson(
        @Json(name = "host_id")
        val hostId: String,
        val name: String,
        val comment: String,
        val date: String,
        @Json(name = "start_time")
        val startTime: String,
        @Json(name = "end_time")
        val endTime: String,
        val place: PostPlaceJson?,
        @Json(name = "group_id")
        val groupId: String
)

data class PostPlaceJson(
        val name: String,
        val address: String,
        val latitude: Double,
        val longitude: Double,
        @Json(name = "place_id")
        val placeId: String,
        @Json(name = "place_type")
        val placeType: String,
        @Json(name = "global_code")
        val globalCode: String?,
        @Json(name = "compound_code")
        val compoundCode: String?,
        val url: String?,
        val memo: String?
) {
        fun toEntity(): EditedPlace {
                return EditedPlace(
                        placeId = placeId,
                        name = name,
                        placeType = placeType,
                        location = Location(
                                lat = latitude,
                                lng = longitude
                        ),
                        formattedAddress = address,
                        plusCode = PlusCode.builder()
                                .setGlobalCode(globalCode)
                                .setCompoundCode(compoundCode)
                                .build(),
                        url = url,
                        memo = memo
                )
        }
}
