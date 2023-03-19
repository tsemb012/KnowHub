package com.example.droidsoftthird.model.domain_model

data class EventDetail (
        val eventId: String,
        val hostId: String,
        val name: String,
        val comment: String,
        val date: String,
        val startTime: String,
        val endTime: String,
        val place: EditedPlace?,
        val groupId: String,
        val groupName: String,
        val registeredUserIds: List<String>
)