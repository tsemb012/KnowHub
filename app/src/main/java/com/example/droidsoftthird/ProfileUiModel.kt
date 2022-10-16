package com.example.droidsoftthird

import android.net.Uri
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.fire_model.LoadState

data class ProfileUiModel (
        val rawUserDetail: UserDetail? = null,
        val editedUserDetail: UserDetail? = null,
        val isSubmitEnabled: Boolean = false,
        val temporalUserImage: Uri? = null,
        val temporalBackgroundImage: Uri? = null,
        val loadState: LoadState = LoadState.Initialized,
) {
    val age = editedUserDetail?.age?.toString() ?: ""
    val area = editedUserDetail?.area.let { it?.prefecture?.name + ", " + it?.city?.name }

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
                isSubmitEnabled = isChangedUserDetail(current) && isNotEmptyUserDetail(current),
                temporalUserImage = _temporalUserImage,
                temporalBackgroundImage = _temporalBackgroundImage,
                loadState = _loadState,
        )

        private fun isChangedUserDetail(current: ProfileUiModel) = current.rawUserDetail != current.editedUserDetail
        private fun isNotEmptyUserDetail(current: ProfileUiModel): Boolean {
            return current.editedUserDetail != null
                && current.editedUserDetail.userName.isNotEmpty()
                && current.editedUserDetail.comment.isNotEmpty()
                //TODO ImageのValidationを追加する。
                && current.editedUserDetail.age != -1
                //TODO Validationを追加していく
        }
    }
}