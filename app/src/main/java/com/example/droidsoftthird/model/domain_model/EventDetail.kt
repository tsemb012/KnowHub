package com.example.droidsoftthird.model.domain_model

import java.time.ZonedDateTime

data class EventDetail (
        val eventId: String,
        val hostId: String,
        val roomId: String, //TODO オンラインでもオフラインでも部屋番号は生成できるように。いつでもオンラインに変更できるように。
        val name: String,
        val comment: String,
        val startDateTime: ZonedDateTime,
        val endDateTime: ZonedDateTime,
        val place: EditedPlace?,
        val groupId: String,
        val groupName: String,
        val registeredUserIds: List<String>
)