package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.utils.converter.formatTimePeriod
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class EventDetail(
    val eventId: String,
    val hostId: String,
    val roomId: String,
    val name: String,
    val comment: String,
    val startDateTime: ZonedDateTime,
    val endDateTime: ZonedDateTime,
    val place: EditedPlace?,
    val groupId: String,
    val groupName: String,
    val registeredUserIds: List<String>,
    val groupMembers: List<SimpleUser>,
    val status: EventStatus,
    val isOnline: Boolean,
) {
    val eventRegisteredMembers: List<SimpleUser> = groupMembers.filter { registeredUserIds.contains(it.userId) }
    val registrationRatio: String = "${registeredUserIds.size}/${groupMembers.size}"
    val formattedDate: String = startDateTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))
    val formattedPeriod = formatTimePeriod(startDateTime, endDateTime, isAmPm = true)
    val formattedStateTime = formattedDate + " " +startDateTime.hour.toString() + "時" + startDateTime.minute.toString() + "分"

}
