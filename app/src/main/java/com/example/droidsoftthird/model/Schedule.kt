package com.example.droidsoftthird.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class Schedule(

    val title: String? = null,
    var date: LocalDate? = null,
    var startTime: LocalDateTime? = null,
    var endTime: LocalDateTime?  = null,
    var place: String? = null, //TODO GEO パートで書き直す
    var groupId: String? = null,
    var members: Map<String, String>? = null,
    @field:JvmField
    var repeat: Boolean? = null,
    @DocumentId
    val scheduleId: String? = null,
    @ServerTimestamp
    val timeStamp: Date? = null

)