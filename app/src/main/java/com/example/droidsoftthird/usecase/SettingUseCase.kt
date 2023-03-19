package com.example.droidsoftthird.usecase

import com.example.droidsoftthird.repository.BaseRepositoryImpl
import javax.inject.Inject

class SettingUseCase @Inject constructor(private val repository: BaseRepositoryImpl) {
    suspend fun getUserId () = repository.getUserId()
}
