package com.example.droidsoftthird

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.droidsoftthird.model.domain_model.fire_model.ScheduleEvent
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.utils.combine
import java.time.LocalDate

open class ScheduleViewModel : ViewModel() {

    protected val schedulesState: MutableLiveData<LoadState> by lazy { MutableLiveData(LoadState.Initialized) }
    private val selectedDate: MutableLiveData<LocalDate> by lazy { MutableLiveData(LocalDate.now()) }
    protected val selectedEvents: MutableLiveData<List<ScheduleEvent>> by lazy { MutableLiveData(emptyList())}
    val uiModel by lazy {
        combine(
            ScheduleUiModel(),
            schedulesState,
            selectedDate,
            selectedEvents,
        ) { current, _schedulesState, _selectedDate, _selectedEvents ->
            ScheduleUiModel(current, _schedulesState, _selectedDate, _selectedEvents)
        }
    }

    fun setSelectedDate(selectedDate: LocalDate) {
        this.selectedDate.value = selectedDate
        selectedEvents.value = uiModel.value?.allEvents?.mapNotNull { scheduleEvent ->
            if (scheduleEvent.date == selectedDate) { scheduleEvent } else null
        }
        schedulesState.value = LoadState.Processed
    }
}
