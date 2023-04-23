package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import com.example.droidsoftthird.model.domain_model.ItemEvent
import com.example.droidsoftthird.model.presentation_model.LoadState
import java.lang.IllegalStateException
import java.time.LocalDate

data class ScheduleUiModel (
    val schedulesLoadState: LoadState = LoadState.Initialized,//TODO State情報
    val allEvents: List<ItemEvent> = emptyList(), //TODO ここにAllを追加する。
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedEvents: List<ItemEvent> = emptyList(),//TODO ここにAllを追加する。
) {
    companion object {
        operator fun invoke(
            current: ScheduleUiModel,
            schedulesLoadState: LoadState,
            selectedDate:LocalDate,
            selectedEvents: List<ItemEvent>
        ) = ScheduleUiModel(
                schedulesLoadState = schedulesLoadState,
                allEvents = schedulesLoadState.getValueOrNull() ?: current.allEvents,
                selectedDate = selectedDate,
                selectedEvents = selectedEvents
        )
    }
}

val LiveData<ScheduleUiModel>.eventDates get() = value?.allEvents?.map { scheduleEvent -> scheduleEvent.period.first.toLocalDate() } ?: emptyList()
val LiveData<ScheduleUiModel>.selectedDate get() = value?.selectedDate ?: throw IllegalStateException()

