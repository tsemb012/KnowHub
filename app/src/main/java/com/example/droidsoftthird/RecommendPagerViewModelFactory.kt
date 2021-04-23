package com.example.droidsoftthird

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecommendPagerViewModelFactory(
    private val repository: UserGroupRepository):ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecommendPagerViewModel::class.java)) {
            return RecommendPagerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

