package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import com.example.droidsoftthird.model.domain_model.fire_model.FireScheduleEvent
import com.example.droidsoftthird.model.presentation_model.LoadState
import java.lang.IllegalStateException
import java.time.LocalDate

data class ScheduleUiModel (
    val schedulesLoadState: LoadState = LoadState.Initialized,//TODO ここをMessageに変更
    val selectedDate: LocalDate = LocalDate.now(),
    val allEvents: List<FireScheduleEvent> = emptyList(),
    val selectedEvents: List<FireScheduleEvent> = emptyList(),//TODO ここにAllを追加する。
) {
    companion object {
        operator fun invoke(
            current: ScheduleUiModel,
            schedulesLoadState: LoadState,
            selectedDate:LocalDate,
            selectedEvents: List<FireScheduleEvent>
        ) = ScheduleUiModel(
                schedulesLoadState = schedulesLoadState,
                selectedDate = selectedDate,
                allEvents = schedulesLoadState.getValueOrNull() ?: emptyList(),
                selectedEvents = selectedEvents
        )
    }
}

val LiveData<ScheduleUiModel>.eventDates get() = value?.schedulesLoadState?.getValueOrNull<List<FireScheduleEvent>>()?.map { scheduleEvent -> scheduleEvent.date } ?: emptyList()
val LiveData<ScheduleUiModel>.selectedDate get() = value?.selectedDate ?: throw IllegalStateException()

