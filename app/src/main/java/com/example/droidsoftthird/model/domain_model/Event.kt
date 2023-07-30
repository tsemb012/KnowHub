package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.R
import com.example.droidsoftthird.model.infra_model.json.request.PostEventJson
import com.example.droidsoftthird.utils.converter.formatTimePeriod
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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

data class EventItem(
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
        val registrationRatio = "$eventRegisteredNumber/$groupJoinedNumber"
        val formattedDate: String = period.first.format(DateTimeFormatter.ofPattern("MM月dd日（E）", Locale.JAPAN))
        val formattedPeriod = formatTimePeriod(period.first, period.second, isAmPm = false)
        val formattedStateTime = formattedDate + " " +period.first.hour.toString() + "時" + period.first.minute.toString() + "分"
}

enum class EventStatus {
        BEFORE_REGISTRATION,
        BEFORE_REGISTRATION_DURING_EVENT,
        AFTER_REGISTRATION_BEFORE_EVENT,
        AFTER_REGISTRATION_DURING_EVENT,
        AFTER_EVENT;

        fun getStatusColor (): Int {
                return when (this) {
                        BEFORE_REGISTRATION -> R.color.primary_accent_yellow
                        BEFORE_REGISTRATION_DURING_EVENT -> R.color.primary_green
                        AFTER_REGISTRATION_BEFORE_EVENT -> R.color.primary_light
                        AFTER_REGISTRATION_DURING_EVENT -> R.color.today_color
                        AFTER_EVENT -> R.color.gray
                }
        }

        fun getStatusText (): String {
                return when (this) {
                        BEFORE_REGISTRATION -> "募集中"
                        BEFORE_REGISTRATION_DURING_EVENT -> "開催中"
                        AFTER_REGISTRATION_BEFORE_EVENT -> "募集終了"
                        AFTER_REGISTRATION_DURING_EVENT -> "開催中"
                        AFTER_EVENT -> "終了"
                }
        }

        fun getStatusDescription (): String {
                return when (this) {
                        BEFORE_REGISTRATION -> "参加者募集中"
                        BEFORE_REGISTRATION_DURING_EVENT -> "開催中です。\n途中参加可能です。"
                        AFTER_REGISTRATION_BEFORE_EVENT -> "開始時刻までお待ちください。"
                        AFTER_REGISTRATION_DURING_EVENT -> "開催中です。\nタップしてチャットを開始してください。"
                        AFTER_EVENT -> "　イベントは終了しました。"
                }
        }
}
