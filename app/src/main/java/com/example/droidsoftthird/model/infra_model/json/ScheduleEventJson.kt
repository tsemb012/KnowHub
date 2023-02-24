package com.example.droidsoftthird.model.infra_model.json

import com.example.droidsoftthird.model.domain_model.ScheduleEventForHome
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import java.time.LocalDate
import java.time.LocalTime

data class ScheduleEventJson (
        @Json(name = "host_id")
        val hostId: String,
        val name: String,
        val comment: String,
        val date: String,
        @Json(name = "start_time")
        val startTime: String,
        @Json(name = "end_time")
        val endTime: String,
        @Json(name = "group_id")
        val groupId: String,
        @Json(name = "group_name")
        val groupName:String,
        @Json(name = "place_name")
        val placeName: String,
) {
    fun toEntity(
            localDateAdapter: JsonAdapter<LocalDate>,
            localTimeAdapter: JsonAdapter<LocalTime>
    ): ScheduleEventForHome {
        return ScheduleEventForHome(
                hostId = hostId,
                name = name,
                comment = comment,
                date = localDateAdapter.fromJson(date) ?: throw IllegalStateException("date is null"),
                period = Pair(
                        localTimeAdapter.fromJson(startTime) ?: throw IllegalStateException("startTime is null"),
                        localTimeAdapter.fromJson(endTime) ?: throw IllegalStateException("endTime is null")
                ),
                groupId = groupId,
                groupName = groupName,
                placeName = placeName,
        )
    }
}
