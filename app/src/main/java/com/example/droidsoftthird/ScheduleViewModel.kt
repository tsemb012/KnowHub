package com.example.droidsoftthird

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.droidsoftthird.model.domain_model.ScheduleEventForHome
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.utils.combine
import java.time.LocalDate

open class ScheduleViewModel : ViewModel() {

    protected val eventsState: MutableLiveData<LoadState> by lazy { MutableLiveData(LoadState.Initialized) }
    private val selectedDate: MutableLiveData<LocalDate> by lazy { MutableLiveData(LocalDate.now()) }
    protected val selectedEvents: MutableLiveData<List<ScheduleEventForHome>> by lazy { MutableLiveData(emptyList())}
    val uiModel by lazy {
        combine(
                ScheduleUiModel(),
                eventsState,
                selectedDate,
                selectedEvents,
        ) { current, _schedulesState, _selectedDate, _selectedEvents ->
            ScheduleUiModel(current, _schedulesState, _selectedDate, _selectedEvents)
        }
    }

    fun setSelectedDate(selectedDate: LocalDate) {
        this.selectedDate.value = selectedDate
        selectedEvents.value = uiModel.value?.allEvents?.mapNotNull { scheduleEventForHome ->
            if (scheduleEventForHome.date == selectedDate) { scheduleEventForHome }
            else null
        }
        eventsState.value = LoadState.Processed
    }
}
