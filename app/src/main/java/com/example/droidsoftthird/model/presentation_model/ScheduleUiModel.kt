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
    val groupFilteredEvents: List<ItemEvent> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedEvents: List<ItemEvent> = emptyList(),
    val simpleGroups: List<SimpleGroup> = emptyList(),
    val selectedSimpleGroup: SimpleGroup? = simpleGroups.firstOrNull(),
    val isGroupFixed: Boolean = false,
) {
    companion object {
        operator fun invoke(
            current: ScheduleUiModel,
            schedulesLoadState: LoadState,
            selectedDate:LocalDate,
            selectedEvents: List<ItemEvent>,
            simpleGroupsLoadState: LoadState,
            selectedGroupId: String,
            isGroupFixed: Boolean,
        ) = ScheduleUiModel(
                isLoading = schedulesLoadState is LoadState.Loading || simpleGroupsLoadState is LoadState.Loading,
                error = schedulesLoadState.getErrorOrNull() ?: simpleGroupsLoadState.getErrorOrNull(),
                notifyType = when (schedulesLoadState) {
                    is LoadState.Loaded<*> -> NotifyType.ALL
                    is LoadState.Processed -> NotifyType.SELECTED
                    else -> NotifyType.NONE
                },
                allEvents = (schedulesLoadState.getValueOrNull() ?: current.allEvents),
                groupFilteredEvents = (schedulesLoadState.getValueOrNull() ?: current.allEvents).filter { if(selectedGroupId.isNotBlank()) it.groupId == selectedGroupId else true },
                selectedDate = selectedDate,
                selectedEvents = selectedEvents,
                simpleGroups = simpleGroupsLoadState.getValueOrNull() ?: current.simpleGroups,
                selectedSimpleGroup = current.simpleGroups.firstOrNull { it.groupId == selectedGroupId },
                isGroupFixed = isGroupFixed,
        )
    }
}

enum class NotifyType { ALL, SELECTED, NONE }

val LiveData<ScheduleUiModel>.eventDates get() = value?.groupFilteredEvents?.map { scheduleEvent -> scheduleEvent.period.first.toLocalDate() } ?: emptyList()
val LiveData<ScheduleUiModel>.selectedDate get() = value?.selectedDate ?: throw IllegalStateException()

