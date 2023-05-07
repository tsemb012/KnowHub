package com.example.droidsoftthird

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.usecase.EventUseCase
import com.example.droidsoftthird.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleCalendarViewModel @Inject constructor(
        private val eventUseCase: EventUseCase,
        private val userUseCase: ProfileUseCase
): ScheduleViewModel(userUseCase){



    fun fetchAllEvents(){

        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            kotlin.runCatching { eventUseCase.fetchEvents() }
                .onSuccess { events ->
                    scheduleLoadState.value = LoadState.Loaded(events)

                }
                .onFailure {
                        e -> scheduleLoadState.value = LoadState.Error(e)
                        Log.d("ScheduleCalendarViewModel", "fetchAllEvents: ${e.message}")
                }
        }
        scheduleLoadState.value = LoadState.Loading(job)
        job.start()
    }

    fun initializeSchedulesState() {
        scheduleLoadState.value = LoadState.Initialized
    }
}
