package com.example.droidsoftthird

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.EditedPlace
import com.example.droidsoftthird.model.presentation_model.SelectedItemStack
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.model.presentation_model.ScheduleCreateUiModel
import com.example.droidsoftthird.usecase.EventUseCase
import com.example.droidsoftthird.usecase.GroupUseCase
import com.example.droidsoftthird.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ScheduleCreateViewModel @Inject constructor(
        private val groupUseCase: GroupUseCase,
        private val eventUseCase: EventUseCase,
): ViewModel() {

    companion object {
        private const val NOT_SET_JPN = "未設定"
        private const val NOT_SET_ENG = "Not set"
    }

    private val _groupsLoadState by lazy { MutableLiveData<LoadState>(LoadState.Initialized) }
    private val _submitLoadState by lazy { MutableLiveData<LoadState>(LoadState.Initialized) }
    private val _selectedItems by lazy { MutableLiveData(SelectedItemStack()) }
    val bindingEventName by lazy { MutableLiveData("") }
    val bindingEventComment by lazy { MutableLiveData("") }

    val groupArray get () = (_groupsLoadState.value?.getValueOrNull() as List<ApiGroup>?)?.map { it.groupName }?.toTypedArray() ?: arrayOf()

    val uiModel: LiveData<ScheduleCreateUiModel> by lazy {
        combine(
                ScheduleCreateUiModel(),
                _groupsLoadState,
                _submitLoadState,
                _selectedItems,
                bindingEventName,
                bindingEventComment
        ) { current, _groupsLoadState, _submitLoadState, _selectedItems, _bindingEventName, _bindingEventComment  ->
            ScheduleCreateUiModel.invoke(current, _groupsLoadState, _submitLoadState, _selectedItems, _bindingEventName, _bindingEventComment)
        }
    }

    fun initializeGroups() {
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            runCatching { groupUseCase.fetchJoinedGroups() }
                .onSuccess {
                    if (it.isNotEmpty()) _groupsLoadState.value = LoadState.Loaded(it)
                    else _groupsLoadState.value = LoadState.Error(Throwable("コミュニティに参加してください。"))
                }
                .onFailure { _groupsLoadState.value = LoadState.Error(it) }
        }
        job.start()
    }

    fun postSelectedDate(selectedDate: LocalDate) {
        _selectedItems.value = _selectedItems.value?.copy(selectedDate = selectedDate)
    }

    fun postTimePeriod(startTime: ZonedDateTime, duration: Duration) { //TODO Editで変革できるように　→　そもそもCalendarじゃなくて別のライブラリを使えたらベスト
        if (uiModel.value?.selectedItems?.selectedDate == null) {
            _selectedItems.value = _selectedItems.value?.copy(startTime = startTime, duration = duration, selectedDate = LocalDate.now())
        } else {
            _selectedItems.value = _selectedItems.value?.copy(startTime = startTime, duration = duration)
        }
    }

    fun postPlace(place: EditedPlace?) {
        _selectedItems.value = _selectedItems.value?.copy(selectedPlace = place)
    }

    fun postSelectedGroup(which: Int) {
        val selectedGroup = _groupsLoadState.value?.getValueOrNull<List<ApiGroup>>()?.get(which)
        _selectedItems.value = _selectedItems.value?.copy(selectedGroup = selectedGroup)
    }

    fun initGroup(groupId: String) {
        val selectedGroup = _groupsLoadState.value?.getValueOrNull<List<ApiGroup>>()?.firstOrNull() { it.groupId == groupId }
        selectedGroup?.let {  _selectedItems.value = _selectedItems.value?.copy(selectedGroup = it) }
    }

    fun postIsOnline(checked: Boolean) {
        _selectedItems.value = _selectedItems.value?.copy(isOnline = checked)
    }

    fun submitEvent() {
        if(uiModel.value?.isSubmitEnabled == true) {
            viewModelScope.launch {
                runCatching { eventUseCase.submitEvent(uiModel.value?.fixedEvent!!) }//TODO !!を消すようにする。
                    .onSuccess {
                        _submitLoadState.value = LoadState.Loaded(it)
                    }
                    .onFailure {
                        _submitLoadState.value = LoadState.Error(IllegalStateException("イベントの登録に失敗しました。"))
                    }
            }
        }
    }
}