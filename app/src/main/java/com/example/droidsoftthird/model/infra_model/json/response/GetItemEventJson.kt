package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.EventStatus
import com.example.droidsoftthird.model.domain_model.EventItem
import com.squareup.moshi.Json
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

data class GetItemEventJson (
        @Json(name = "id")
        val eventId : String,
        @Json(name = "host_id")
        val hostId: String,
        val name: String,
        val comment: String,
        @Json(name = "start_date_time")
        val startDateTime: String,
        @Json(name = "end_date_time")
        val endDateTime: String,
        @Json(name = "group_id")
        val groupId: String,
        @Json(name = "group_name")
        val groupName:String?,
        @Json(name = "place_name")
        val placeName: String?,
        @Json(name = "event_registered_number")
        val eventRegisteredNumber: Int,
        @Json(name = "group_joined_number")
        val groupJoinedNumber: Int,
        @Json(name = "event_status")
        val status: String,
        @Json(name = "is_online")
        val isOnline: Boolean,
) {
    fun toEntity(): EventItem {
        return EventItem(
                    eventId = eventId,
                    hostId = hostId,
                    name = name,
                    comment = comment,
                    period = Pair(
                            ZonedDateTime.ofInstant(Instant.parse(startDateTime) , ZoneId.systemDefault()),
                            ZonedDateTime.ofInstant(Instant.parse(endDateTime) , ZoneId.systemDefault())
                    ),
                    groupId = groupId,
                    groupName = groupName,
                    placeName = if(placeName.isNullOrBlank()) "オンライン" else placeName,
                    eventRegisteredNumber = eventRegisteredNumber,
                    groupJoinedNumber = groupJoinedNumber,
                    status = EventStatus.valueOf(status.uppercase()),
                    isOnline = isOnline
                )
    }
}
