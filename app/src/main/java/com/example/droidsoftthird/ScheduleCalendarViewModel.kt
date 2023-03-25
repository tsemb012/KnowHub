package com.example.droidsoftthird

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
                    sacheduleLoadState.value = LoadState.Loaded(events)
                }
                .onFailure {
                        e -> sacheduleLoadState.value = LoadState.Error(e)
                }
        }
        sacheduleLoadState.value = LoadState.Loading(job)
        job.start()
    }

    fun initializeSchedulesState() {
        sacheduleLoadState.value = LoadState.Initialized
    }
}
