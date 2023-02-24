package com.example.droidsoftthird

import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.usecase.EventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleRegisteredViewModel @Inject constructor(
        private val userCase: EventUseCase,
): ScheduleViewModel(){


    fun fetchAllEvents(){

        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            kotlin.runCatching { userCase.fetchEvents() }
                .onSuccess { events ->
                    eventsState.value = LoadState.Loaded(events)
                }
                .onFailure {
                        e -> eventsState.value = LoadState.Error(e)
                }
        }
        eventsState.value = LoadState.Loading(job)
        job.start()
    }

    fun initializeSchedulesState() {
        eventsState.value = LoadState.Initialized
    }
}
