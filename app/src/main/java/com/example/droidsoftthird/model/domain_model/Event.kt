package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.infra_model.json.request.PostEventJson
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
        fun toJson(): PostEventJson {
                return PostEventJson(
                        hostId = hostId ?: throw IllegalStateException("hostId is null"),
                        name = name,
                        comment = comment,
                        startDateTime = period.first.format(DateTimeFormatter.ISO_INSTANT),
                        endDateTime = period.second.format(DateTimeFormatter.ISO_INSTANT),
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
        val eventRegisteredNumber: Int,
        val groupJoinedNumber: Int,
        val status: EventStatus,
        val isOnline: Boolean,
) :Event() {
        val registrationRatio = "$eventRegisteredNumber / $groupJoinedNumber"
}

enum class EventStatus {
        BEFORE_REGISTRATION,
        AFTER_REGISTRATION_BEFORE_EVENT,
        AFTER_REGISTRATION_DURING_EVENT,
        AFTER_EVENT,
}
