package com.example.droidsoftthird.model.presentation_model

import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.EditedPlace
import com.example.droidsoftthird.model.domain_model.CreateEvent
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class ScheduleCreateUiModel (
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    private val groups: List<ApiGroup>? = null,
    val selectedItems: SelectedItemStack = SelectedItemStack(),
    private val bindingUiName: String? = null,
    private val bindingUiComment: String? = null,
) {
    val uiDate = selectedItems.selectedDate?.toString()?.format("yyyy/MM/dd") ?: NO_SETTING
    val uiPeriod = selectedItems.let {
        val startTime = it.startTime?.toLocalTime()
        val startTimeFormatted = startTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
        val endTime = it.startTime?.plus(it.duration)?.toLocalTime()
        val endTimeFormatted = endTime?.format(DateTimeFormatter.ofPattern("HH:mm"))

        val isNextDay = startTime?.isAfter(endTime) ?: false

        if (startTimeFormatted == null || endTimeFormatted == null) NO_SETTING
        else if (isNextDay) "$startTimeFormatted - 翌$endTimeFormatted"
        else "$startTimeFormatted - $endTimeFormatted"
    }
    val uiPlace = if (selectedItems.isOnline == true) "オンライン" else selectedItems.selectedPlace?.name ?: NO_SETTING
    val uiGroup = selectedItems.selectedGroup?.groupName ?: NO_SETTING

    private val zonedStartTime get() = selectedItems.startTime?.with(selectedItems.selectedDate)
    private val zonedEndTime get() = zonedStartTime?.plus(selectedItems.duration)

    val fixedEvent: CreateEvent get() = CreateEvent(
            name = bindingUiName ?: throw IllegalStateException("name is null"),
            comment = bindingUiComment ?: throw IllegalStateException("comment is null"),
            period = Pair(
                zonedStartTime ?: throw IllegalStateException("startTime is null"),
                zonedEndTime ?: throw IllegalStateException("endTime is null"),
            ),
            place = if (selectedItems.isOnline == true) null else selectedItems.selectedPlace,
            groupId = selectedItems.selectedGroup?.groupId ?: throw IllegalStateException("group is null"),
    )

    val isSubmitEnabled get() = isValid(
            bindingUiName,
            bindingUiComment,
            selectedItems.selectedDate,
            zonedStartTime to zonedEndTime,
            selectedItems.selectedPlace,
            selectedItems.selectedGroup,
            selectedItems.isOnline == true,
    )

    companion object {
        operator fun invoke(
            current: ScheduleCreateUiModel,
            groupsLoadState: LoadState,
            _selectedItems: SelectedItemStack,
            _bindingEventName: String,
            _bindingEventComment: String,
        ) = ScheduleCreateUiModel(
                    isLoading = groupsLoadState is LoadState.Loading,
                    error = groupsLoadState.getErrorOrNull(),
                    groups = groupsLoadState.getValueOrNull(),
                    selectedItems = _selectedItems,
                    bindingUiName = _bindingEventName,
                    bindingUiComment = _bindingEventComment,
        )

        private const val NO_SETTING = "未設定"

        private fun isValid(
            userName: String?,
            comment: String?,
            selectedDate: LocalDate?,
            selectedPeriod: Pair<ZonedDateTime?, ZonedDateTime?>,
            selectedPlace: EditedPlace?,
            selectedGroup: ApiGroup?,
            isOnline: Boolean,
        ):Boolean {
            return (
                    userName != null && userName.isNotBlank() &&
                    comment != null && comment.isNotBlank() &&
                    selectedDate != null &&
                    selectedPeriod.first != null &&
                    selectedPeriod.second != null &&
                    selectedGroup != null &&
                    ((!isOnline && selectedPlace != null) || isOnline)
                )
        }
    }
}

