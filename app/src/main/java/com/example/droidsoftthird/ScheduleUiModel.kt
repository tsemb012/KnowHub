package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import com.example.droidsoftthird.model.domain_model.ScheduleEventForHome
import com.example.droidsoftthird.model.presentation_model.LoadState
import java.lang.IllegalStateException
import java.time.LocalDate

data class ScheduleUiModel (
    val schedulesLoadState: LoadState = LoadState.Initialized,//TODO State情報
    val selectedDate: LocalDate = LocalDate.now(),
    val allEvents: List<ScheduleEventForHome> = emptyList(), //TODO ここにAllを追加する。
    val selectedEvents: List<ScheduleEventForHome> = emptyList(),//TODO ここにAllを追加する。
) {
    companion object {
        operator fun invoke(
            current: ScheduleUiModel,
            schedulesLoadState: LoadState,
            selectedDate:LocalDate,
            selectedEvents: List<ScheduleEventForHome>
        ) = ScheduleUiModel(
                schedulesLoadState = schedulesLoadState,
                selectedDate = selectedDate,
                allEvents = schedulesLoadState.getValueOrNull() ?: emptyList(),
                selectedEvents = selectedEvents
        )
    }
}

val LiveData<ScheduleUiModel>.eventDates get() = value?.schedulesLoadState?.getValueOrNull<List<ScheduleEventForHome>>()?.map { scheduleEvent -> scheduleEvent.date } ?: emptyList()
val LiveData<ScheduleUiModel>.selectedDate get() = value?.selectedDate ?: throw IllegalStateException()

