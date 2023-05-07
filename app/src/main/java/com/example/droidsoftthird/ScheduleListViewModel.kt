package com.example.droidsoftthird

import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.usecase.EventUseCase
import com.example.droidsoftthird.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleListViewModel @Inject constructor(
    eventUseCase: EventUseCase,
    userUseCase: ProfileUseCase
): ScheduleViewModel(eventUseCase, userUseCase)
