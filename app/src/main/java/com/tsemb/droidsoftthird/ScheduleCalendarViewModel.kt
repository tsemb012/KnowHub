package com.tsemb.droidsoftthird

import com.tsemb.droidsoftthird.usecase.EventUseCase
import com.tsemb.droidsoftthird.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleCalendarViewModel @Inject constructor(
    eventUseCase: EventUseCase,
    userUseCase: ProfileUseCase
): ScheduleViewModel(eventUseCase, userUseCase) //TODO 将来的にクラスが分かれることを考慮して派生ViewModelを残しておく
