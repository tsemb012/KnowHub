package com.example.droidsoftthird

import androidx.lifecycle.ViewModel
import com.example.droidsoftthird.usecase.GroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class GroupLocationsViewModel @Inject constructor(private val useCase: GroupUseCase) : ViewModel() {




}