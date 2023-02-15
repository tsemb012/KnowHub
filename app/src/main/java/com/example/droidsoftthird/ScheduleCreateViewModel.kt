package com.example.droidsoftthird

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.EditedPlaceDetail
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.model.presentation_model.ScheduleCreateUiModel
import com.example.droidsoftthird.usecase.GroupUseCase
import com.example.droidsoftthird.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ScheduleCreateViewModel @Inject constructor(
        private val groupUseCase: GroupUseCase,
        //private val eventUseCase: EventUseCase,
): ViewModel() {

    companion object {
        private const val NOT_SET_JPN = "未設定"
        private const val NOT_SET_ENG = "Not set"
    }

    private val _selectedDate by lazy { MutableLiveData<LocalDate>(null) }
    private val _selectedPeriod by lazy { MutableLiveData<Pair<Calendar, Calendar>>(null) }
    private val _selectedPlace by lazy { MutableLiveData<EditedPlaceDetail>(null) }
    private val _selectedGroup by lazy { MutableLiveData<ApiGroup>(null) }
    private val _groupsLoadState by lazy { MutableLiveData<LoadState>() }

    val groupArray get () = (_groupsLoadState.value?.getValueOrNull() as List<ApiGroup>?)?.map { it.groupName }?.toTypedArray() ?: arrayOf()

    val uiModel by lazy {
        combine(
                ScheduleCreateUiModel(),
                _groupsLoadState,
                _selectedDate,
                _selectedPeriod,
                _selectedPlace,
                _selectedGroup,
        ) { current, _groupsLoadState, _selectedDate, _selectedPeriod, _selectedPlace, _selectedGroup ->
            ScheduleCreateUiModel.invoke(current, _groupsLoadState, _selectedDate, _selectedPeriod, _selectedPlace, _selectedGroup)
        }
    }

    fun initializeGroups() {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            runCatching { groupUseCase.fetchJoinedGroups() }
                .onSuccess { _groupsLoadState.value = LoadState.Loaded(it) }
                .onFailure { _groupsLoadState.value = LoadState.Error(it) }
        }
        job.start()
    }

    fun postSelectedDate(selectedDate: LocalDate) {
        _selectedDate.value = selectedDate
    }

    fun postTimePeriod(startTime: Calendar, endTime: Calendar) {
        _selectedPeriod.value = Pair(startTime, endTime)
    }

    fun postPlace(place: EditedPlaceDetail) {
        _selectedPlace.postValue(place)
    }

    fun postSelectedGroup(which: Int) {
        val selectedGroup = _groupsLoadState.value?.getValueOrNull<List<ApiGroup>>()?.get(which)
        _selectedGroup.value = selectedGroup
    }

}