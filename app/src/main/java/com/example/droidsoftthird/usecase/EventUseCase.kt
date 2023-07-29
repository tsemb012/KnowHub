package com.example.droidsoftthird.usecase

import com.example.droidsoftthird.model.domain_model.CreateEvent
import com.example.droidsoftthird.model.domain_model.EventDetail
import com.example.droidsoftthird.model.domain_model.SimpleUser
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import javax.inject.Inject

class EventUseCase @Inject constructor(private val repository: BaseRepositoryImpl) {
    suspend fun submitEvent(event: CreateEvent) = repository.createEvent(event)
    suspend fun fetchEvents() = repository.fetchEvents()
    suspend fun fetchEventDetail(eventId: String): EventDetail {
        val eventDetail = repository.fetchEventDetail(eventId)
        return eventDetail.copy(
            groupMembers = eventDetail.groupMembers.map { user ->
                SimpleUser(
                    userId = user.userId,
                    userName = user.userName,
                    userImage = fetchUserImage(user.userImage)
                )
            }
        )
    }
    private suspend fun fetchUserImage(imagePath: String) = repository.fetchStorageImage(imagePath)
    suspend fun joinEvent(eventId: String) = repository.registerEvent(eventId)
    suspend fun leaveEvent(eventId: String) = repository.unregisterEvent(eventId)
    suspend fun deleteEvent(eventId: String) = repository.deleteEvent(eventId)
}
