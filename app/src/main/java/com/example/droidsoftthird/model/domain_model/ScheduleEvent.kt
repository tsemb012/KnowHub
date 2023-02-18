package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.infra_model.json.request.PostScheduleEventJson
import com.squareup.moshi.JsonAdapter
import java.time.LocalDate
import java.time.LocalTime

data class ScheduleEvent(
        val hostId: String? = null,
        val name: String,
        val comment: String,
        val date: LocalDate,
        val period: Pair<LocalTime, LocalTime>,
        val place: EditedPlace,
        val groupId: String,
) {
        fun toJson(localDateAdapter: JsonAdapter<LocalDate>, localTimeAdapter: JsonAdapter<LocalTime>): PostScheduleEventJson {
                val (start, end) = period
                return PostScheduleEventJson(
                        hostId = hostId ?: throw IllegalStateException("hostId is null"),
                        name = name,
                        comment = comment,
                        date = localDateAdapter.toJson(date),
                        start = localTimeAdapter.toJson(start),
                        end = localTimeAdapter.toJson(end),
                        place = place.toJson(),
                        groupId = groupId
                )
        }
}
