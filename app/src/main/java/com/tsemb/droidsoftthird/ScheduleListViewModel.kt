package com.tsemb.droidsoftthird

import com.tsemb.droidsoftthird.usecase.EventUseCase
import com.tsemb.droidsoftthird.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleListViewModel @Inject constructor(
    eventUseCase: EventUseCase,
    userUseCase: ProfileUseCase
): ScheduleViewModel(eventUseCase, userUseCase)
