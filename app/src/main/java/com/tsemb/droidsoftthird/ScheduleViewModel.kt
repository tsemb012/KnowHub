package com.tsemb.droidsoftthird

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsemb.droidsoftthird.model.domain_model.EventItem
import com.tsemb.droidsoftthird.model.presentation_model.LoadState
import com.tsemb.droidsoftthird.model.presentation_model.ScheduleUiModel
import com.tsemb.droidsoftthird.usecase.EventUseCase
import com.tsemb.droidsoftthird.usecase.ProfileUseCase
import com.tsemb.droidsoftthird.utils.combine
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import java.time.LocalDate

open class ScheduleViewModel(
    private val eventUseCase: EventUseCase,
    private val userUseCase: ProfileUseCase
    ) : ViewModel() {

    private val _scheduleLoadState: MutableLiveData<LoadState> by lazy { MutableLiveData(LoadState.Initialized) }
    private val _selectedDate: MutableLiveData<LocalDate> by lazy { MutableLiveData(LocalDate.now()) }
    private val _selectedEvents: MutableLiveData<List<EventItem>> by lazy { MutableLiveData(emptyList())}
    private val _simpleGroupsLoadState: MutableLiveData<LoadState> by lazy { MutableLiveData(
        LoadState.Initialized) }
    private val _selectedGroup: MutableLiveData<String> by lazy { MutableLiveData("") }
    private var _isGroupFixed: MutableLiveData<Boolean> = MutableLiveData(false)
    val uiModel by lazy {
        combine(
                ScheduleUiModel(),
                _scheduleLoadState,
                _selectedDate,
                _selectedEvents,
                _simpleGroupsLoadState,
                _selectedGroup,
                _isGroupFixed,
        ) { current, _schedulesState, _selectedDate, _selectedEvents, _groupIdsLoadState, _selectedGroup, _isGroupFixed ->
            ScheduleUiModel(current, _schedulesState, _selectedDate, _selectedEvents, _groupIdsLoadState, _selectedGroup, _isGroupFixed)
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

    fun setIsNavigatedFromChatGroup(isNavigatedFrom: Boolean) {
        _isGroupFixed.value = isNavigatedFrom
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
