package com.example.droidsoftthird.usecase

import com.example.droidsoftthird.model.domain_model.CreateEvent
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import javax.inject.Inject

class EventUseCase @Inject constructor(private val repository: BaseRepositoryImpl) {
    suspend fun submitEvent(event: CreateEvent) = repository.createEvent(event)
    suspend fun fetchEvents() = repository.fetchEvents()
    suspend fun fetchEventDetail(eventId: String) = repository.fetchEventDetail(eventId)
    suspend fun joinEvent(eventId: String) = repository.registerEvent(eventId)
    suspend fun leaveEvent(eventId: String) = repository.unregisterEvent(eventId)
}
