package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.EditedPlace
import com.example.droidsoftthird.model.domain_model.EventDetail
import com.example.droidsoftthird.model.domain_model.Location
import com.google.android.libraries.places.api.model.PlusCode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import java.time.LocalDate
import java.time.LocalTime

data class GetEventDetailJson (
        @Json(name = "id")
        val eventId: String,
        @Json(name = "host_id")
        val hostId: String,
        @Json(name = "video_chat_room_id")
        val roomId: String,
        val name: String,
        val comment: String,
        val date: String,
        @Json(name = "start_time")
        val startTime: String,
        @Json(name = "end_time")
        val endTime: String,
        val place: GetEventPlaceJson?,
        @Json(name = "group_id")
        val groupId: String,
        @Json(name = "group_name")
        val groupName: String,
        @Json(name = "registered_user_ids")
        val registeredUserIds: List<String>
) {
        fun toEntity(
            localDateAdapter: JsonAdapter<LocalDate>,
            localTimeAdapter: JsonAdapter<LocalTime>
        ): EventDetail = EventDetail(
                eventId = eventId,
                hostId = hostId,
                roomId = roomId,
                name = name,
                comment = comment,
                date = localDateAdapter.fromJson(date) ?: throw IllegalStateException("date is null"),
                startTime = localTimeAdapter.fromJson(startTime) ?: throw IllegalStateException("startTime is null"),
                endTime = localTimeAdapter.fromJson(endTime) ?: throw IllegalStateException("endTime is null"),
                place = place?.toEntity(),
                groupId = groupId,
                groupName = groupName,
                registeredUserIds = registeredUserIds
        )

        data class GetEventPlaceJson(
            val id: String,
            @Json(name = "place_id")
            val placeId: String,
            val name: String,
            val address: String,
            val latitude: Double,
            val longitude: Double,
            @Json(name = "place_type")
            val placeType: String,
            @Json(name = "global_code")
            val globalCode: String?,
            @Json(name = "compound_code")
            val compoundCode: String?,
            val url: String?,
            val memo: String?
    ) {
                fun toEntity(): EditedPlace = EditedPlace(
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