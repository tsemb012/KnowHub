package com.example.droidsoftthird.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDate
import java.util.*

data class Schedule(

    val scheduleId: String? = null,
    var scheduleName: String? = null,
    var groupIntroduction: String? = null,
    var groupType: String? = null,
    var prefecture: String? = null,
    var city: String? = null,
    var facilityEnvironment: String? = null,
    var basis: String? = null,
    var frequency:String? = null,
    var minAge:Int? = null,
    var maxAge:Int? = null,
    var minNumberPerson:Int? = null,
    var maxNumberPerson :Int? = null,
    @field:JvmField
    var isChecked:Boolean? = null,
    @DocumentId
    val groupId: String? = null,
    @ServerTimestamp
    val timeStamp: Date? = null

)