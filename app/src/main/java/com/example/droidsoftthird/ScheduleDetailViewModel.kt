package com.example.droidsoftthird

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.EventDetail
import com.example.droidsoftthird.usecase.EventUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class ScheduleDetailViewModel @AssistedInject constructor(
    private val useCase: EventUseCase,
    @Assisted private val eventId:String,
): ViewModel() {

    val eventDetail = mutableStateOf<EventDetail?>(null)
    private val message = mutableStateOf<String?>(null)


    fun initialize() {
        viewModelScope.launch {
            kotlin.runCatching { useCase.fetchEventDetail(eventId) }
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



    @AssistedFactory
    interface Factory {
        fun create(eventId: String): ScheduleDetailViewModel
    }
}