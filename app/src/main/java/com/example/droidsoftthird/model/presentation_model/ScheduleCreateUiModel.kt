package com.example.droidsoftthird.model.presentation_model

import androidx.lifecycle.MutableLiveData
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.EditedPlaceDetail
import com.example.droidsoftthird.model.domain_model.UserDetail
import java.time.LocalDate
import java.util.*

data class ScheduleCreateUiModel (
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    private val groups: List<ApiGroup>? = null,
    private val selectedDate: LocalDate? = null,
    private val selectedPeriod: Pair<Calendar, Calendar>? = null,
    private val selectedPlace: EditedPlaceDetail? = null,
    private val selectedGroup: ApiGroup? = null,
) {

    val bindingEventName = MutableLiveData<String>()
    val bindingEventComment = MutableLiveData<String>()

    val uiDate = selectedDate?.toString()?.format("yyyy/MM/dd") ?: NO_SETTING
    val uiPeriod = selectedPeriod?.let { "${it.first.get(Calendar.HOUR_OF_DAY)}:${it.first.get(Calendar.MINUTE)} - ${it.second.get(Calendar.HOUR_OF_DAY)}:${it.second.get(Calendar.MINUTE)}" } ?: NO_SETTING
    val uiPlace = selectedPlace?.name ?: NO_SETTING
    val uiGroup = selectedGroup?.groupName ?: NO_SETTING

    val isSubmitEnabled = isValid(
            bindingEventName.value,
            bindingEventComment.value,
            selectedDate,
            selectedPeriod,
            selectedPlace,
            selectedGroup,
    )
    companion object {
        operator fun invoke(
                current: ScheduleCreateUiModel,
                groupsLoadState: LoadState,
                _selectedDate: LocalDate?,
                _selectedPeriod: Pair<Calendar, Calendar>?,
                _selectedPlace: EditedPlaceDetail?,
                _selectedGroup: ApiGroup?,
        ) = ScheduleCreateUiModel(
                    isLoading = groupsLoadState is LoadState.Loading,
                    error = groupsLoadState.getErrorOrNull(),
                    groups = groupsLoadState.getValueOrNull(),
                    selectedDate = _selectedDate,
                    selectedPeriod = _selectedPeriod,
                    selectedPlace = _selectedPlace,
                    selectedGroup = _selectedGroup,
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
                userName.isNullOrBlank() &&
                comment.isNullOrBlank() &&
                selectedDate != null &&
                selectedPeriod != null &&
                selectedPlace != null &&
                selectedGroup != null

        private fun isChangedUserDetail(raw: UserDetail, edited: UserDetail) = raw != edited
        private fun isStoredTemporalImages(temporalUserImage: Map<String, String>) = temporalUserImage.isNotEmpty()
        private fun isNotEmptyUserDetail(edited: UserDetail) =
            edited.gender.isNotBlank() &&
                edited.area != null &&
                edited.age != -1
    }
}

