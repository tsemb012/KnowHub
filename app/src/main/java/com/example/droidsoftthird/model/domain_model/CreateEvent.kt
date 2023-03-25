package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.infra_model.json.request.PostEventJson
import com.squareup.moshi.JsonAdapter
import java.time.LocalDate
import java.time.LocalTime

abstract class Event {
        abstract val hostId: String?
        abstract val name: String
        abstract val comment: String
        abstract val date: LocalDate
        abstract val period: Pair<LocalTime, LocalTime>
        abstract val groupId: String
}

data class CreateEvent(
        override val hostId: String? = null,
        override val name: String,
        override val comment: String,
        override val date: LocalDate,
        override val period: Pair<LocalTime, LocalTime>,
        val place: EditedPlace? = null,
        override val groupId: String,
): Event() {
        fun toJson(localDateAdapter: JsonAdapter<LocalDate>, localTimeAdapter: JsonAdapter<LocalTime>): PostEventJson {
                val (start, end) = period
                return PostEventJson(
                        hostId = hostId ?: throw IllegalStateException("hostId is null"),
                        name = name,
                        comment = comment,
                        date = localDateAdapter.toJson(date),
                        startTime = localTimeAdapter.toJson(start),
                        endTime = localTimeAdapter.toJson(end),
                        place = place?.toJson(),
                        groupId = groupId
                )
        }
}

data class ItemEvent(
        val eventId: String,
        override val hostId: String? = null,
        override val name: String,
        override val comment: String,
        override val date: LocalDate,
        override val period: Pair<LocalTime, LocalTime>,
        override val groupId: String,
        val groupName: String?,
        val placeName: String?,
) :Event()
