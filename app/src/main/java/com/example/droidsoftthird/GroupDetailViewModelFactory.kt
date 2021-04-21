package com.example.droidsoftthird

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GroupDetailViewModelFactory (private val groupId:String, private val repository: GroupRepository): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupDetailViewModel::class.java)) {
            return GroupDetailViewModel(groupId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}