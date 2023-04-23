package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.ItemEvent
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import java.time.ZonedDateTime

data class GetItemEventJson (
        @Json(name = "id")
        val eventId : String,
        @Json(name = "host_id")
        val hostId: String,
        val name: String,
        val comment: String,
        @Json(name = "start_time")
        val startTime: String,
        @Json(name = "end_time")
        val endTime: String,
        @Json(name = "group_id")
        val groupId: String,
        @Json(name = "group_name")
        val groupName:String?,
        @Json(name = "place_name")
        val placeName: String?,
) {
    fun toEntity(
            zonedDateTimeJsonAdapter: JsonAdapter<ZonedDateTime>,
    ): ItemEvent {
        return ItemEvent(
                eventId = eventId,
                hostId = hostId,
                name = name,
                comment = comment,
                period = Pair(
                        zonedDateTimeJsonAdapter.fromJson(startTime) ?: throw IllegalStateException("startTime is null"),
                        zonedDateTimeJsonAdapter.fromJson(endTime) ?: throw IllegalStateException("endTime is null")
                ),
                groupId = groupId,
                groupName = groupName,
                placeName = if(placeName.isNullOrBlank()) "オンライン" else placeName
        )
    }
}
