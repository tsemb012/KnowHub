package com.example.droidsoftthird.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.lang.IllegalStateException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

data class SchedulePlan(//空欄にして良い場所はNullableにしておく。
    val title: String,
    val date: LocalDate,
    val place: Place?,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val groupId: String,
    val schedulePlanId: String,
    val timeStamp: Date,
    )
//TODO 繰り返し設定を追加する。
//TODO CloudFunctionを使って、group情報をすでに保存されているGroupデータから引き出したい。

data class RawSchedulePlan(
    var title: String? = null,
    var date: LocalDate? = null,
    var place: HashMap<String,Any> = hashMapOf(
        "isOnline" to true,
        "prefecture" to "",
        "city" to "",
        "latitude" to 0.1234567,
        "longitude" to 0.1234567,
    ),
    var startTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
    var groupId: String? = null,
    @DocumentId
    var schedulePlanId: String? = null,
    @ServerTimestamp
    var timeStamp: Date? = null,
)
//TODO HashMapの書き方をこれであっているか確認する

fun RawSchedulePlan.toEntity() = SchedulePlan(
    title = title ?: throw IllegalStateException(),
    date = date ?: throw IllegalStateException(),
    place = Place(
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
    timeStamp = timeStamp ?: throw IllegalStateException(),
)


