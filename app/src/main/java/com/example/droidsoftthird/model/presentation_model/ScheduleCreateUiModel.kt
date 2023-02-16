package com.example.droidsoftthird.model.presentation_model

import androidx.lifecycle.MutableLiveData
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.EditedPlaceDetail
import com.example.droidsoftthird.model.domain_model.EventItemStack
import java.time.LocalDate
import java.util.*

data class ScheduleCreateUiModel (
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    private val groups: List<ApiGroup>? = null,
    private val selectedItems: EventItemStack = EventItemStack(),
    private val bindingUiName: String? = null,
    private val bindingUiComment: MutableLiveData<String> = MutableLiveData(""),
) {
    val uiDate = selectedItems.selectedDate?.toString()?.format("yyyy/MM/dd") ?: NO_SETTING
    val uiPeriod = selectedItems.selectedPeriod?.let { "${it.first.get(Calendar.HOUR_OF_DAY)}:${it.first.get(Calendar.MINUTE)} - ${it.second.get(Calendar.HOUR_OF_DAY)}:${it.second.get(Calendar.MINUTE)}" } ?: NO_SETTING
    val uiPlace = selectedItems.selectedPlace?.name ?: NO_SETTING
    val uiGroup = selectedItems.selectedGroup?.groupName ?: NO_SETTING

    val isSubmitEnabled = isValid(
            bindingUiName,
            bindingUiComment.value,
            selectedItems.selectedDate,
            selectedItems.selectedPeriod,
            selectedItems.selectedPlace,
            selectedItems.selectedGroup,
    )
    companion object {
        operator fun invoke(
                current: ScheduleCreateUiModel,
                groupsLoadState: LoadState,
                _selectedItems: EventItemStack,
                _bindingEventName: String,
        ) = ScheduleCreateUiModel(
                    isLoading = groupsLoadState is LoadState.Loading,
                    error = groupsLoadState.getErrorOrNull(),
                    groups = groupsLoadState.getValueOrNull(),
                    selectedItems = _selectedItems,
                    bindingUiName = _bindingEventName,
        )

        private const val NO_SETTING = "未設定"

        private fun isValid(
                userName: String?,
                comment: String?,
                selectedDate: LocalDate?,
                selectedPeriod: Pair<Calendar, Calendar>?,
                selectedPlace: EditedPlaceDetail?,
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

