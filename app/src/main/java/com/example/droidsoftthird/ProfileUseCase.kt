package com.example.droidsoftthird

import com.example.droidsoftthird.repository.BaseRepositoryImpl
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val repository: BaseRepositoryImpl) {

    suspend fun fetchUserDetail() = repository.fetchUser()

}
