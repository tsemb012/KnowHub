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
    val age = editedUserDetail.age.toString()
    val area = editedUserDetail.area.let { it?.prefecture?.name + ", " + it?.city?.name }

    companion object {
        operator fun invoke(
                current: ProfileUiModel,
                _rawUserDetail: UserDetail,
                _editedUserDetail: UserDetail,
                _temporalUserImage: Uri,
                _temporalBackgroundImage: Uri,
                _loadState: LoadState,
        ) = ProfileUiModel(
                rawUserDetail = _rawUserDetail,
                editedUserDetail = _editedUserDetail,
                isSubmitEnabled = isValid(current),
                temporalUserImage = _temporalUserImage,
                temporalBackgroundImage = _temporalBackgroundImage,
                loadState = _loadState,
        )

        private fun isValid(current: ProfileUiModel) =
                isChangedUserDetail(current) &&
                isStoredTemporalImages(current) &&
                isNotEmptyUserDetail(current)


        private fun isChangedUserDetail(current: ProfileUiModel) =
                current.rawUserDetail != current.editedUserDetail
        private fun isStoredTemporalImages(current: ProfileUiModel) =
                current.temporalUserImage != null
                && current.temporalBackgroundImage != null
        private fun isNotEmptyUserDetail(current: ProfileUiModel) =
                current.editedUserDetail.userName.isNotBlank()
                && current.editedUserDetail.comment.isNotBlank()
                && current.editedUserDetail.gender.isNotBlank()
                && current.editedUserDetail.comment.isNotEmpty()
                && current.editedUserDetail.area != null
                && current.editedUserDetail.age != -1
        }

    }
