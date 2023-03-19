package com.example.droidsoftthird.model.domain_model

import java.time.LocalDate
import java.time.LocalTime

data class EventDetail (
        val eventId: String,
        val hostId: String,
        val name: String,
        val comment: String,
        val date: LocalDate,
        val startTime: LocalTime,
        val endTime: LocalTime,
        val place: EditedPlace?,
        val groupId: String,
        val groupName: String,
        val registeredUserIds: List<String>
)