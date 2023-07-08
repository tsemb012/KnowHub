package com.example.droidsoftthird.utils.converter

import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun periodConverter(startTime: ZonedDateTime?, duration: Duration?, isAmPm: Boolean = false): String {
    return when {
        startTime == null || duration == null -> "未設定"
        else -> {
            val endTime = startTime.plus(duration)

            s(isAmPm, startTime, endTime)
        }
    }
}

private fun s(
    isAmPm: Boolean,
    startTime: ZonedDateTime,
    endTime: ZonedDateTime,
): String {
    val pattern = if (isAmPm) "hh:mm a" else "HH:mm"
    val timeFormatter = DateTimeFormatter.ofPattern(pattern)
    val formattedStartTime = startTime.toLocalTime().format(timeFormatter)
    val formattedEndTime = endTime.toLocalTime().format(timeFormatter)

    return if (startTime.toLocalTime().isAfter(endTime.toLocalTime()))
        "$formattedStartTime - 翌$formattedEndTime"
    else
        "$formattedStartTime - $formattedEndTime"
}