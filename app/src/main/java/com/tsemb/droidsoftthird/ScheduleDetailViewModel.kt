package com.tsemb.droidsoftthird

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsemb.droidsoftthird.model.domain_model.EventDetail
import com.tsemb.droidsoftthird.usecase.EventUseCase
import com.tsemb.droidsoftthird.usecase.SettingUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class ScheduleDetailViewModel @AssistedInject constructor(
    private val eventUseCase: EventUseCase,
    private val settingUseCase: SettingUseCase,
    @Assisted private val eventId:String,
): ViewModel() {

    val eventDetail = mutableStateOf<EventDetail?>(null)
    val isLoading = mutableStateOf(false)
    val isJoined = mutableStateOf(false)
    val message = mutableStateOf<String?>(null)
    val userId by lazy { runBlocking { settingUseCase.getUserId() } }
    private val startDateTime get() = eventDetail.value?.startDateTime?.toLocalDateTime()
    private val endDateTime get() = eventDetail.value?.endDateTime?.toLocalDateTime()
    val isVideoChatAvailable get () = LocalDateTime.now().isAfter(startDateTime) && LocalDateTime.now().isBefore(endDateTime)
    val isNotStarted get () = LocalDateTime.now().isBefore(startDateTime)
    val isFinished get () = LocalDateTime.now().isAfter(endDateTime)

    fun fetchEventDetail() {
        val job = viewModelScope.launch {
            runCatching { eventUseCase.fetchEventDetail(eventId) }
                .onSuccess {
                    eventDetail.value = it
                    isLoading.value = false
                    isJoined.value = it.registeredUserIds.contains(userId)
                }
                .onFailure {
                    message.value = it.message
                    isLoading.value = false
                }
        }
        isLoading.value = true
        job.start()
    }

    fun joinEvent() {
        val job = viewModelScope.launch {
            kotlin.runCatching { eventUseCase.joinEvent(eventId) }
                .onSuccess {
                    message.value = "参加しました"
                    fetchEventDetail()
                    isLoading.value = false
                }
                .onFailure {
                    message.value = it.message
                    isLoading.value = false
                }
        }
        isLoading.value = true
        job.start()
    }

    fun leaveEvent() {
        val job = viewModelScope.launch {
                kotlin.runCatching { eventUseCase.leaveEvent(eventId) }
                    .onSuccess {
                        message.value = "参加を取り消しました"
                        fetchEventDetail()
                        isLoading.value = false
                    }
                    .onFailure {
                        message.value = it.message
                        isLoading.value = false
                    }
            }
        isLoading.value = true
        job.start()
    }

    fun deleteEvent() {
        val job = viewModelScope.launch {
            kotlin.runCatching { eventUseCase.deleteEvent(eventId) }
                .onSuccess {
                    message.value = "イベントを削除しました"
                    isLoading.value = false
                }
                .onFailure {
                    message.value = it.message
                    isLoading.value = false
                }
        }
        isLoading.value = true
        job.start()
    }
    @AssistedFactory
    interface Factory {
        fun create(eventId: String): ScheduleDetailViewModel
    }
}