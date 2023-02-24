package com.example.droidsoftthird.model.presentation_model

import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.EditedPlace
import com.example.droidsoftthird.model.domain_model.ScheduleEvent
import com.example.droidsoftthird.model.domain_model.SelectedItemStack
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class ScheduleCreateUiModel (
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    private val groups: List<ApiGroup>? = null,
    private val selectedItems: SelectedItemStack = SelectedItemStack(),
    private val bindingUiName: String? = null,
    private val bindingUiComment: String? = null,
) {
    val uiDate = selectedItems.selectedDate?.toString()?.format("yyyy/MM/dd") ?: NO_SETTING
    val uiPeriod = selectedItems.selectedPeriod?.let { "${it.first.format(DateTimeFormatter.ofPattern("HH:mm:ss"))}:${it.first.format(DateTimeFormatter.ofPattern("HH:mm:ss"))} - ${it.second.format(DateTimeFormatter.ofPattern("HH:mm:ss"))}:${it.second.format(DateTimeFormatter.ofPattern("HH:mm:ss"))}" } ?: NO_SETTING
    val uiPlace = selectedItems.selectedPlace?.name ?: NO_SETTING
    val uiGroup = selectedItems.selectedGroup?.groupName ?: NO_SETTING
    val fixedEvent: ScheduleEvent get() = ScheduleEvent(
            name = bindingUiName ?: throw IllegalStateException("name is null"),
            comment = bindingUiComment ?: throw IllegalStateException("comment is null"),
            date = selectedItems.selectedDate ?: throw IllegalStateException("date is null"),
            period = selectedItems.selectedPeriod ?: throw IllegalStateException("period is null"),
            place = selectedItems.selectedPlace ?: throw IllegalStateException("place is null"),
            groupId = selectedItems.selectedGroup?.groupId ?: throw IllegalStateException("group is null"),
    )
    val isSubmitEnabled = isValid(
            bindingUiName,
            bindingUiComment,
            selectedItems.selectedDate,
            selectedItems.selectedPeriod,
            selectedItems.selectedPlace,
            selectedItems.selectedGroup,
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
                selectedPeriod: Pair<LocalTime, LocalTime>?,
                selectedPlace: EditedPlace?,
                selectedGroup: ApiGroup?,
        ) =
                userName != null && userName.isNotBlank() &&
                comment != null && comment.isNotBlank() &&
                selectedDate != null &&
                selectedPeriod != null &&
                selectedPlace != null &&
                selectedGroup != null
    }
}

