package com.example.droidsoftthird

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

    private val _scheduleLoadState: MutableLiveData<LoadState> by lazy { MutableLiveData(LoadState.Initialized) }
    private val _selectedDate: MutableLiveData<LocalDate> by lazy { MutableLiveData(LocalDate.now()) }
    private val _selectedEvents: MutableLiveData<List<ItemEvent>> by lazy { MutableLiveData(emptyList())}
    private val _simpleGroupsLoadState: MutableLiveData<LoadState> by lazy { MutableLiveData(LoadState.Initialized) }
    private val _selectedGroup: MutableLiveData<String> by lazy { MutableLiveData("") }
    val uiModel by lazy {
        combine(
                ScheduleUiModel(),
                _scheduleLoadState,
                _selectedDate,
                _selectedEvents,
                _simpleGroupsLoadState,
                _selectedGroup
        ) { current, _schedulesState, _selectedDate, _selectedEvents, _groupIdsLoadState, _selectedGroup ->
            ScheduleUiModel(current, _schedulesState, _selectedDate, _selectedEvents, _groupIdsLoadState, _selectedGroup)
        }
    }

    fun fetchAllEvents(){
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            kotlin.runCatching { eventUseCase.fetchEvents() }
                .onSuccess { events ->
                    _scheduleLoadState.value = LoadState.Loaded(events)
                }
                .onFailure {
                        e -> _scheduleLoadState.value = LoadState.Error(e)
                }
        }
        _scheduleLoadState.value = LoadState.Loading(job)
        job.start()
    }


    fun setSelectedDate(selectedDate: LocalDate) {
        this._selectedDate.value = selectedDate
        _selectedEvents.value = uiModel.value?.groupFilteredEvents?.mapNotNull { scheduleEventForHome ->
            if (scheduleEventForHome.period.first.toLocalDate() == selectedDate) { scheduleEventForHome }
            else null
        }
        _scheduleLoadState.value = LoadState.Processed
    }

    fun setSelectedGroupId(selectedGroupId: String) {
        _selectedGroup.value = selectedGroupId
    }

    fun fetchSimpleGroups() {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            kotlin.runCatching { userUseCase.fetchUserJoinedSimpleGroups() }
                .onSuccess { groupIds ->
                    _simpleGroupsLoadState.value = LoadState.Loaded(groupIds)
                }
                .onFailure {
                        e -> _simpleGroupsLoadState.value = LoadState.Error(e)
                }
        }
        _simpleGroupsLoadState.value = LoadState.Loading(job)
        job.start()
    }
}
