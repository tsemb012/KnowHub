package com.example.droidsoftthird.usecase

import com.example.droidsoftthird.model.domain_model.ScheduleEvent
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import javax.inject.Inject

class EventUseCase @Inject constructor(private val repository: BaseRepositoryImpl) {
    suspend fun submitEvent(event: ScheduleEvent) = repository.createEvent(event)
}
