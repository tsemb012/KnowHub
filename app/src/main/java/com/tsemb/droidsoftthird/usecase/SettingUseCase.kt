package com.tsemb.droidsoftthird.usecase

import com.tsemb.droidsoftthird.repository.BaseRepositoryImpl
import javax.inject.Inject

class SettingUseCase @Inject constructor(private val repository: BaseRepositoryImpl) {
    suspend fun getUserId () = repository.getUserId()
}
