package com.example.droidsoftthird

import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.fire_model.LoadState

data class ProfileUiModel (
        val rawUserDetail: UserDetail? = null,
        val editedUserDetail: UserDetail? = null,
        val isSubmitEnabled: Boolean = false,
        val loadState: LoadState = LoadState.Initialized,
) {
    companion object {
        operator fun invoke(
                current: ProfileUiModel,
                _rawUserDetail: UserDetail,
                _editedUserDetail: UserDetail,
                _loadState: LoadState,
        ) = ProfileUiModel(
                rawUserDetail = _rawUserDetail,
                editedUserDetail = _editedUserDetail,
                isSubmitEnabled = isChangedUserDetail(current) && isNotEmptyUserDetail(current),
                loadState = _loadState,
        )

        private fun isChangedUserDetail(current: ProfileUiModel) = current.rawUserDetail != current.editedUserDetail
        private fun isNotEmptyUserDetail(current: ProfileUiModel): Boolean {
            return current.editedUserDetail != null
                && current.editedUserDetail.userName.isNotEmpty()
                && current.editedUserDetail.comment.isNotEmpty()
                && current.editedUserDetail.userImage.isNotEmpty()
                && current.editedUserDetail.backgroundImage.isNotEmpty()
                && current.editedUserDetail.age != -1
                //TODO Validationを追加していく
        }
    }
}
