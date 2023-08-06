package com.tsemb.droidsoftthird.utils.converter

import java.time.LocalTime
import java.util.*

fun Calendar.convertToLocalTime(): LocalTime {
    val hour = this.get(Calendar.HOUR_OF_DAY)
    val minute = this.get(Calendar.MINUTE)
    val second = this.get(Calendar.SECOND)
    val millisecond = this.get(Calendar.MILLISECOND)
    return LocalTime.of(hour, minute, second, millisecond * 1000000)
}
