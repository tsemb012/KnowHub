package com.example.droidsoftthird

import android.net.Uri
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.domain_model.initializedUserDetail
import com.example.droidsoftthird.model.fire_model.LoadState

data class ProfileUiModel (
        val rawUserDetail: UserDetail = initializedUserDetail,
        val editedUserDetail: UserDetail = initializedUserDetail,
        val isSubmitEnabled: Boolean = false,
        val temporalUserImage: Uri? = null,
        val temporalBackgroundImage: Uri? = null,

        val loadState: LoadState = LoadState.Initialized,
) {
    val gender = editedUserDetail.gender.let { it.ifBlank { NO_SETTING } }
    val age = editedUserDetail.age.toString().let { if(it == "-1") NO_SETTING else  it }
    val area = editedUserDetail.area.let {
        if (it == null)  NO_SETTING
        else if (it.city == null) it.prefecture.name
        else it?.prefecture?.name + ", " + it?.city?.name
    }

    companion object {
        operator fun invoke(
                current: ProfileUiModel,
                _rawUserDetail: UserDetail,
                _editedUserDetail: UserDetail,
                _temporalUserImage: Uri,
                _temporalBackgroundImage: Uri,
                isTextFilled: Boolean,
                _loadState: LoadState,
        ) = ProfileUiModel(
                rawUserDetail = _rawUserDetail,
                editedUserDetail = _editedUserDetail,
                isSubmitEnabled = isValid(current, isTextFilled),
                temporalUserImage = _temporalUserImage,
                temporalBackgroundImage = _temporalBackgroundImage,
                loadState = _loadState,
        )

        private const val NO_SETTING = "未設定"

        private fun isValid(current: ProfileUiModel, isTextFilled: Boolean) =
                isChangedUserDetail(current) &&
                isStoredTemporalImages(current) &&
                isNotEmptyUserDetail(current) &&
                isTextFilled

        private fun isChangedUserDetail(current: ProfileUiModel) =
                current.rawUserDetail != current.editedUserDetail
        private fun isStoredTemporalImages(current: ProfileUiModel) =
                current.temporalUserImage != null
                && current.temporalBackgroundImage != null
        private fun isNotEmptyUserDetail(current: ProfileUiModel) =
                   current.editedUserDetail.gender.isNotBlank()
                && current.editedUserDetail.area != null
                && current.editedUserDetail.age != -1
        }

    }
