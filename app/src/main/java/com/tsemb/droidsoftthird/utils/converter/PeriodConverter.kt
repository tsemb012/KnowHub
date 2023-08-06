package com.tsemb.droidsoftthird.utils.converter

import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun convertPeriodFromDuration(startTime: ZonedDateTime?, duration: Duration?, isAmPm: Boolean = false): String {
    return when {
        startTime == null || duration == null -> "未設定"
        else -> {
            val endTime = startTime.plus(duration)

            formatTimePeriod(startTime, endTime, isAmPm)
        }
    }
}

fun formatTimePeriod(
    startTime: ZonedDateTime,
    endTime: ZonedDateTime,
    isAmPm: Boolean = true,
    ): String {
    val pattern = if (isAmPm) "ahh:mm" else "HH:mm"
    val timeFormatter = DateTimeFormatter.ofPattern(pattern) // ロケールを英語に設定
    val formattedStartTime = startTime.toLocalTime().format(timeFormatter)
    val formattedEndTime = endTime.toLocalTime().format(timeFormatter)

    return if (startTime.toLocalTime().isAfter(endTime.toLocalTime()))
        if (isAmPm) "$formattedStartTime - $formattedEndTime(翌)" else "$formattedStartTime - 翌$formattedEndTime"
    else
        "$formattedStartTime - $formattedEndTime"
}