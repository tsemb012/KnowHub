package com.example.droidsoftthird

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.ItemEvent
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.model.presentation_model.ScheduleUiModel
import com.example.droidsoftthird.usecase.EventUseCase
import com.example.droidsoftthird.usecase.ProfileUseCase
import com.example.droidsoftthird.utils.combine
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import java.time.LocalDate

open class ScheduleViewModel(
    private val eventUseCase: EventUseCase,
    private val userUseCase: ProfileUseCase
    ) : ViewModel() {

    protected val scheduleLoadState: MutableLiveData<LoadState> by lazy { MutableLiveData(LoadState.Initialized) }
    private val selectedDate: MutableLiveData<LocalDate> by lazy { MutableLiveData(LocalDate.now()) }
    protected val selectedEvents: MutableLiveData<List<ItemEvent>> by lazy { MutableLiveData(emptyList())}
    private val groupIdsLoadState: MutableLiveData<LoadState> by lazy { MutableLiveData(LoadState.Initialized) }
    val uiModel by lazy {
        combine(
                ScheduleUiModel(),
                scheduleLoadState,
                selectedDate,
                selectedEvents,
                groupIdsLoadState,
        ) { current, _schedulesState, _selectedDate, _selectedEvents, _groupIdsLoadState ->
            ScheduleUiModel(current, _schedulesState, _selectedDate, _selectedEvents, _groupIdsLoadState)
        }
    }

    fun fetchAllEvents(){
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            kotlin.runCatching { eventUseCase.fetchEvents() }
                .onSuccess { events ->
                    scheduleLoadState.value = LoadState.Loaded(events)
                }
                .onFailure {
                        e -> scheduleLoadState.value = LoadState.Error(e)
                }
        }
        scheduleLoadState.value = LoadState.Loading(job)
        job.start()
    }
    fun initializeSchedulesState() {
        scheduleLoadState.value = LoadState.Initialized
    }
    fun setSelectedDate(selectedDate: LocalDate) {
        this.selectedDate.value = selectedDate
        selectedEvents.value = uiModel.value?.allEvents?.mapNotNull { scheduleEventForHome ->
            if (scheduleEventForHome.period.first.toLocalDate() == selectedDate) { scheduleEventForHome }
            else null
        }
        scheduleLoadState.value = LoadState.Processed
    }

    fun fetchGroupIds() {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            kotlin.runCatching { userUseCase.fetchUserJoinedGroupIds() }
                .onSuccess { groupIds ->
                    groupIdsLoadState.value = LoadState.Loaded(groupIds)
                }
                .onFailure {
                        e -> groupIdsLoadState.value = LoadState.Error(e)
                }
        }
        groupIdsLoadState.value = LoadState.Loading(job)
        job.start()
    }
}
