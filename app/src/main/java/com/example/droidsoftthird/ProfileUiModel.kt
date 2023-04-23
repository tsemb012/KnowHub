package com.example.droidsoftthird

import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.domain_model.initializedUserDetail
import com.example.droidsoftthird.model.presentation_model.LoadState
import java.time.LocalDate
import java.time.Period

data class ProfileUiModel (
    val rawUserDetail: UserDetail = initializedUserDetail,
    val editedUserDetail: UserDetail = initializedUserDetail,
    val temporalUserImage: Map<String, String>? = null,
    val isSubmitEnabled: Boolean = false,
    val loadState: LoadState = LoadState.Initialized,
) {
    val gender = editedUserDetail.gender.let { it.ifBlank { NO_SETTING } }
    val age =
        if (editedUserDetail.birthday == LocalDate.now()) NO_SETTING
        else {
            val age = Period.between(editedUserDetail.birthday, LocalDate.now()).years
            val yearMonth = "${editedUserDetail.birthday.year}年${editedUserDetail.birthday.monthValue}月"
            "$yearMonth ($age 歳)"
        }
    val area = editedUserDetail.area.let {
        if (it.city == null) it.prefecture?.name ?: NO_SETTING
        else it.prefecture?.name + ", " + it.city.name
    }

    companion object {
        operator fun invoke(
            current: ProfileUiModel,
            _rawUserDetail: UserDetail,
            _editedUserDetail: UserDetail,
            _temporalUserImage: Map<String, String>,
            isTextFilled: Boolean,
            _loadState: LoadState,
        ) = ProfileUiModel(
                rawUserDetail = _rawUserDetail,
                editedUserDetail = _editedUserDetail,
                temporalUserImage = _temporalUserImage,
                isSubmitEnabled = isValid(_rawUserDetail, _editedUserDetail, _temporalUserImage, isTextFilled),
                loadState = _loadState,
        )

        private const val NO_SETTING = "未設定"

        private fun isValid(
                rawUserDetail: UserDetail,
                editedUserDetail: UserDetail,
                temporalUserImage: Map<String, String>,
                isTextFilled: Boolean
        ) =
                isChangedUserDetail(rawUserDetail, editedUserDetail) &&
                isStoredTemporalImages(temporalUserImage) &&
                isNotEmptyUserDetail(editedUserDetail) &&
                isTextFilled

        private fun isChangedUserDetail(raw: UserDetail, edited: UserDetail) = raw != edited
        private fun isStoredTemporalImages(temporalUserImage: Map<String, String>) = temporalUserImage.isNotEmpty()
        private fun isNotEmptyUserDetail(edited: UserDetail) =
                edited.gender.isNotBlank() &&
                edited.area != null &&
                edited.birthday != LocalDate.now()
        }

    }
