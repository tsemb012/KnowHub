package com.example.droidsoftthird.model.presentation_model

import androidx.lifecycle.LiveData
import com.example.droidsoftthird.model.domain_model.ItemEvent
import com.example.droidsoftthird.model.domain_model.SimpleGroup
import java.lang.IllegalStateException
import java.time.LocalDate

data class ScheduleUiModel (
    val isLoading: Boolean = false,
    val error : Throwable? = null,
    val notifyType: NotifyType = NotifyType.NONE,
    val allEvents: List<ItemEvent> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedEvents: List<ItemEvent> = emptyList(),
    val simpleGroups: List<SimpleGroup> = emptyList(),
) {
    companion object {
        operator fun invoke(
            current: ScheduleUiModel,
            schedulesLoadState: LoadState,
            selectedDate:LocalDate,
            selectedEvents: List<ItemEvent>,
            groupIdsLoadState: LoadState,
        ) = ScheduleUiModel(
                isLoading = schedulesLoadState is LoadState.Loading || groupIdsLoadState is LoadState.Loading,
                error = schedulesLoadState.getErrorOrNull() ?: groupIdsLoadState.getErrorOrNull(),
                notifyType = when (schedulesLoadState) {
                    is LoadState.Loaded<*> -> NotifyType.ALL
                    is LoadState.Processed -> NotifyType.SELECTED
                    else -> NotifyType.NONE
                },
                allEvents = schedulesLoadState.getValueOrNull() ?: current.allEvents,
                selectedDate = selectedDate,
                selectedEvents = selectedEvents,
                simpleGroups = groupIdsLoadState.getValueOrNull() ?: current.simpleGroups,
        )
    }
}

enum class NotifyType { ALL, SELECTED, NONE }

val LiveData<ScheduleUiModel>.eventDates get() = value?.allEvents?.map { scheduleEvent -> scheduleEvent.period.first.toLocalDate() } ?: emptyList()
val LiveData<ScheduleUiModel>.selectedDate get() = value?.selectedDate ?: throw IllegalStateException()

