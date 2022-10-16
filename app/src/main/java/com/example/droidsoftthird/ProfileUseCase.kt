package com.example.droidsoftthird

import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val repository: BaseRepositoryImpl) {
    //TODO 中身が複雑化してきた場合、インターフェースをつけてわかりやすくする。現状は不要。

    suspend fun fetchUserDetail() = repository.fetchUser()
    suspend fun updateUserDetail(userDetail: UserDetail) = repository.updateUserDetail(userDetail)


}
