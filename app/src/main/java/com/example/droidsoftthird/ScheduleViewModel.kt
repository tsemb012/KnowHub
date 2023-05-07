package com.example.droidsoftthird

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.droidsoftthird.model.domain_model.ItemEvent
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.model.presentation_model.ScheduleUiModel
import com.example.droidsoftthird.utils.combine
import java.time.LocalDate

open class ScheduleViewModel : ViewModel() {

    protected val scheduleLoadState: MutableLiveData<LoadState> by lazy { MutableLiveData(LoadState.Initialized) }
    private val selectedDate: MutableLiveData<LocalDate> by lazy { MutableLiveData(LocalDate.now()) }
    protected val selectedEvents: MutableLiveData<List<ItemEvent>> by lazy { MutableLiveData(emptyList())}
    val uiModel by lazy {
        combine(
                ScheduleUiModel(),
                scheduleLoadState,
                selectedDate,
                selectedEvents,
        ) { current, _schedulesState, _selectedDate, _selectedEvents ->
            ScheduleUiModel(current, _schedulesState, _selectedDate, _selectedEvents)
        }
    }

    fun setSelectedDate(selectedDate: LocalDate) {
        this.selectedDate.value = selectedDate
        selectedEvents.value = uiModel.value?.allEvents?.mapNotNull { scheduleEventForHome ->
            if (scheduleEventForHome.period.first.toLocalDate() == selectedDate) { scheduleEventForHome }
            else null
        }
        scheduleLoadState.value = LoadState.Processed
    }
}
