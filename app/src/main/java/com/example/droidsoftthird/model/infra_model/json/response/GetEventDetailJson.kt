package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.EditedPlace
import com.example.droidsoftthird.model.domain_model.EventDetail
import com.example.droidsoftthird.model.domain_model.EventStatus
import com.example.droidsoftthird.model.domain_model.Location
import com.google.android.libraries.places.api.model.PlusCode
import com.squareup.moshi.Json
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

data class GetEventDetailJson (
    @Json(name = "id")
    val eventId: String,
    @Json(name = "host_id")
    val hostId: String,
    @Json(name = "video_chat_room_id")
    val roomId: String,
    val name: String,
    val comment: String,
    @Json(name = "start_date_time")
    val startDateTime: String,
    @Json(name = "end_date_time")
    val endDateTime: String,
    val place: GetEventPlaceJson?,
    @Json(name = "group_id")
    val groupId: String,
    @Json(name = "group_name")
    val groupName: String,
    @Json(name = "registered_user_ids")
    val registeredUserIds: List<String>,
    @Json(name = "group_members")
    val groupMembers: List<GetSimpleUserJson>,
    @Json(name = "event_status")
    val status: String,
    @Json(name = "is_online")
    val isOnline: Boolean,
) {
        fun toEntity() =
            EventDetail(
                eventId = eventId,
                hostId = hostId,
                roomId = roomId,
                name = name,
                comment = comment,
                startDateTime = ZonedDateTime.ofInstant(Instant.parse(startDateTime) , ZoneId.systemDefault()),
                endDateTime = ZonedDateTime.ofInstant(Instant.parse(endDateTime) , ZoneId.systemDefault()),
                place = place?.toEntity(),
                groupId = groupId,
                groupName = groupName,
                registeredUserIds = registeredUserIds,
                groupMembers = groupMembers.map { it.toEntity() },
                status = EventStatus.valueOf(status.uppercase()),
                isOnline = isOnline
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