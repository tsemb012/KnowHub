package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import com.example.droidsoftthird.model.domain_model.fire_model.LoadState
import com.example.droidsoftthird.model.domain_model.fire_model.ScheduleEvent
import java.lang.IllegalStateException
import java.time.LocalDate

data class ScheduleUiModel (
    val schedulesLoadState: LoadState = LoadState.Initialized,//TODO ここをMessageに変更
    val selectedDate: LocalDate = LocalDate.now(),
    val allEvents: List<ScheduleEvent> = emptyList(),
    val selectedEvents: List<ScheduleEvent> = emptyList(),//TODO ここにAllを追加する。
) {
    companion object {
        operator fun invoke(
            current: ScheduleUiModel,
            schedulesLoadState: LoadState,
            selectedDate:LocalDate,
            selectedEvents: List<ScheduleEvent>
        ) = ScheduleUiModel(
                schedulesLoadState = schedulesLoadState,
                selectedDate = selectedDate,
                allEvents = schedulesLoadState.getValueOrNull() ?: emptyList(),
                selectedEvents = selectedEvents
        )
    }
}

val LiveData<ScheduleUiModel>.eventDates get() = value?.schedulesLoadState?.getValueOrNull<List<ScheduleEvent>>()?.map { scheduleEvent -> scheduleEvent.date } ?: emptyList()
val LiveData<ScheduleUiModel>.selectedDate get() = value?.selectedDate ?: throw IllegalStateException()

