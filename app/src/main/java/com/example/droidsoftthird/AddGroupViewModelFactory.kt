package com.example.droidsoftthird

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddGroupViewModelFactory(
    private val repository: GroupRepository):ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddGroupViewModel::class.java)) {
            return AddGroupViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}