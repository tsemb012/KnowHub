package com.example.droidsoftthird

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.EventDetail
import com.example.droidsoftthird.usecase.EventUseCase
import com.example.droidsoftthird.usecase.SettingUseCase
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
    val message = mutableStateOf<String?>(null)
    val userId by lazy { runBlocking { settingUseCase.getUserId() } }
    private val startDateTime get() = LocalDateTime.of(eventDetail.value?.date, eventDetail.value?.startTime)
    private val endDateTime get() = LocalDateTime.of(eventDetail.value?.date, eventDetail.value?.endTime)
    val isVideoChatAvailable get () = LocalDateTime.now().isAfter(startDateTime) && LocalDateTime.now().isBefore(endDateTime)
    val isNotStarted get () = LocalDateTime.now().isBefore(startDateTime)
    val isFinished get () = LocalDateTime.now().isAfter(endDateTime)

    fun fetchEventDetail() {
        viewModelScope.launch {
            kotlin.runCatching { eventUseCase.fetchEventDetail(eventId) }
                .onSuccess {
                    eventDetail.value = it
                    Log.d("tsemb012", "$eventDetail")
                }
                .onFailure {
                    message.value = it.message
                    Log.d("tsemb012", "${it.message}")
                }
        }
    }

    fun joinEvent() {
        val job = viewModelScope.launch {
            kotlin.runCatching { eventUseCase.joinEvent(eventId) }
                .onSuccess {
                    message.value = "参加しました"
                    Log.d("tsemb012", "参加しました")
                    fetchEventDetail()
                }
                .onFailure {
                    message.value = it.message
                    Log.d("tsemb012", "${it.message}")
                }
        }
        job.start()
    }

    fun leaveEvent() {
        val job = viewModelScope.launch {
                kotlin.runCatching { eventUseCase.leaveEvent(eventId) }
                    .onSuccess {
                        message.value = "参加を取り消しました"
                        Log.d("tsemb012", "参加を取り消しました")
                        fetchEventDetail()
                    }
                    .onFailure {
                        message.value = it.message
                        Log.d("tsemb012", "${it.message}")
                    }
            }
        job.start()
    }

    fun deleteEvent() {
        val job = viewModelScope.launch {
            kotlin.runCatching { eventUseCase.deleteEvent(eventId) }
                .onSuccess {
                    message.value = "イベントを削除しました"
                    Log.d("tsemb012", "イベントを削除しました")
                    //TODO 削除したあとの導線も検討するようにする。
                }
                .onFailure {
                    message.value = it.message
                    Log.d("tsemb012", "${it.message}")
                }
        }
        job.start()
    }
    @AssistedFactory
    interface Factory {
        fun create(eventId: String): ScheduleDetailViewModel
    }
}