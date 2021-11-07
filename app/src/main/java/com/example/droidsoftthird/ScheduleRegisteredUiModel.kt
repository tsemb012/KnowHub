package com.example.droidsoftthird

import com.example.droidsoftthird.model.LoadState
import com.example.droidsoftthird.model.SchedulePlan

data class ScheduleRegisteredUiModel (
    val schedulesLoadState: LoadState = LoadState.Initialized
    ) {
    companion object {
        operator fun invoke(
            current: ScheduleRegisteredUiModel,
            schedulesLoadState: LoadState
        ) = ScheduleRegisteredUiModel(
            schedulesLoadState = schedulesLoadState
        )
    }
}