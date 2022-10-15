package com.example.droidsoftthird

import com.example.droidsoftthird.model.domain_model.Area
import com.example.droidsoftthird.model.domain_model.UserDetail

data class ProfileUiModel (
        val rawUserDetail: UserDetail? = null,
        val userDetail: UserDetail? = null,
        val isSubmitEnabled: Boolean = false,
    ) {
    companion object {
        operator fun invoke(
                current: ProfileUiModel,
                _name: String,
                _comment: String,
                _userImage: String,
                _backgroundImage: String,
                _rawUserDetail: UserDetail,
                _age: Int,
                _area: Area,
                _gender: String,
        ) = ProfileUiModel(
                rawUserDetail = _rawUserDetail,
                userDetail = current.userDetail?.copy(
                        userName = _name,
                        comment = _comment,
                        userImage = _userImage,
                        backgroundImage = _backgroundImage,
                        age= _age,
                        area = _area,
                        gender = _gender
                ),
                isSubmitEnabled =
                    isChangedUserDetail(current) && isNotEmptyUserDetail(current)
        )

        private fun isChangedUserDetail(current: ProfileUiModel) = current.rawUserDetail != current.userDetail
        private fun isNotEmptyUserDetail(current: ProfileUiModel): Boolean {
            return current.userDetail != null
                && current.userDetail.userName.isNotEmpty()
                && current.userDetail.comment.isNotEmpty()
                && current.userDetail.userImage.isNotEmpty()
                && current.userDetail.backgroundImage.isNotEmpty()
                && current.userDetail.age != -1
        }
    }
}
