package com.example.droidsoftthird.model.domain_model.fire_model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.lang.IllegalStateException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

data class FireScheduleEvent(//空欄にして良い場所はNullableにしておく。
    val title: String,
    val date: LocalDate,
    val place: FirePlace?,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
    val groupId: String,
    val schedulePlanId: String,
    val timeStamp: LocalDateTime,
    )
//TODO 繰り返し設定を追加する。
//TODO CloudFunctionを使って、group情報をすでに保存されているGroupデータから引き出したい。

data class RawScheduleEvent(
    var title: String? = null,
    var date: LocalDate? = null,
    var place: HashMap<String,Any> = hashMapOf(
        "isOnline" to true,
        "prefecture" to "",
        "city" to "",
        "latitude" to 0.1234567,
        "longitude" to 0.1234567,
    ),
    var startTime: LocalTime? = null,
    var endTime: LocalTime? = null,
    var groupId: String? = null,
    @DocumentId
    var schedulePlanId: String? = null,
    @ServerTimestamp
    var timeStamp: Date? = null,
)
//TODO HashMapの書き方をこれであっているか確認する

fun RawScheduleEvent.toEntity() = FireScheduleEvent(
    title = title ?: throw IllegalStateException(),
    date = date ?: throw IllegalStateException(),
    place = FirePlace(
        place["isOnline"] as Boolean,
        place["prefecture"] as String,
        place["city"] as String,
        place["latitude"] as Double,
        place["longitude"] as Double
    ),
    startTime = startTime,
    endTime = endTime,
    groupId = groupId ?: throw IllegalStateException(),
    schedulePlanId = schedulePlanId ?: throw IllegalStateException(),
    timeStamp = LocalDateTime.ofInstant(timeStamp?.toInstant(), ZoneId.systemDefault() ),
)


