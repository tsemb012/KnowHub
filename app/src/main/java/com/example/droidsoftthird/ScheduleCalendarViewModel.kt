package com.example.droidsoftthird

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.usecase.EventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleCalendarViewModel @Inject constructor(
        private val userCase: EventUseCase,
): ScheduleViewModel(){


    fun fetchAllEvents(){

        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            kotlin.runCatching { userCase.fetchEvents() }
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
