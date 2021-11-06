package com.example.droidsoftthird.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

data class SchedulePlan(
    val title: String,
    val date: Date,
    val place: Place,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val groupId: String,
    val schedulePlanId: String,
    val timeStamp: Date,
    )
//TODO 繰り返し設定を追加する。
//TODO CloudFunctionを使って、group情報をすでに保存されているGroupデータから引き出したい。

data class RawSchedulePlan(
    var title: String? = null,
    var date: LocalDate? = null,
    var place: HashMap<String,Any>? = null,
    var startTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
    var groupId: String? = null,
    @DocumentId
    var schedulePlanId: String? = null,
    @ServerTimestamp
    var timeStamp: Date? = null,
)


