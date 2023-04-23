package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.infra_model.json.request.PostEventJson
import com.squareup.moshi.JsonAdapter
import java.time.ZonedDateTime

abstract class Event {
        abstract val hostId: String?
        abstract val name: String
        abstract val comment: String
        abstract val period: Pair<ZonedDateTime, ZonedDateTime>
        abstract val groupId: String
}

data class CreateEvent(
        override val hostId: String? = null,
        override val name: String,
        override val comment: String,
        override val period: Pair<ZonedDateTime, ZonedDateTime>,
        val place: EditedPlace? = null,
        override val groupId: String,
): Event() {
        fun toJson(localDateAdapter: JsonAdapter<ZonedDateTime>): PostEventJson {
                return PostEventJson(
                        hostId = hostId ?: throw IllegalStateException("hostId is null"),
                        name = name,
                        comment = comment,
                        startDateTime = localDateAdapter.toJson(period.first),
                        endDateTime = localDateAdapter.toJson(period.second),
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
        override val period: Pair<ZonedDateTime, ZonedDateTime>,
        override val groupId: String,
        val groupName: String?,
        val placeName: String?,
) :Event()
