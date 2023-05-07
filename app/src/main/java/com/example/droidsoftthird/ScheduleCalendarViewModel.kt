package com.example.droidsoftthird

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.usecase.EventUseCase
import com.example.droidsoftthird.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleCalendarViewModel @Inject constructor(
        eventUseCase: EventUseCase,
        userUseCase: ProfileUseCase
): ScheduleViewModel(eventUseCase, userUseCase) //TODO 将来的にクラスが分かれることを考慮して派生ViewModelを残しておく
