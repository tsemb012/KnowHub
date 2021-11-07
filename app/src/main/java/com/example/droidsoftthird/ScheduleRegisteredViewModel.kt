package com.example.droidsoftthird

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.LoadState
import com.example.droidsoftthird.model.toEntity
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.example.droidsoftthird.utils.combine
import kotlinx.coroutines.launch

class ScheduleRegisteredViewModel @ViewModelInject constructor(private val repository: BaseRepositoryImpl<*>): ViewModel() {

    private val _schedulesState: MutableLiveData<LoadState> by lazy { MutableLiveData(LoadState.Initialized) }
    val uiModel by lazy {
        combine(
            ScheduleRegisteredUiModel(),
            _schedulesState
        ) { current, schedulesState -> ScheduleRegisteredUiModel(current, schedulesState) }
    }

    private fun getSchedules(query: String){
        val job = viewModelScope.launch {
                repository.getSchedules(query).apply {
                    when(this) {
                        is Result.Success -> _schedulesState.value = LoadState.Loaded(data.map { it.toEntity() } )
                        is Result.Failure -> _schedulesState.value = LoadState.Error(exception)
                    }
                }
        }
        _schedulesState.value = LoadState.Loading(job)
        job.start()
    }

    fun initializeSchedulesState() {
        _schedulesState.value = LoadState.Initialized
    }
}
