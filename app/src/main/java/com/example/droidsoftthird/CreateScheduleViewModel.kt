package com.example.droidsoftthird

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.droidsoftthird.repository.ScheduleRepository

class CreateScheduleViewModel @ViewModelInject constructor(private val repository: ScheduleRepository): ViewModel() {





    fun postStartTime(startHour: Int?, startMinute: Int?) {
        TODO("ここでLocalDateTimeに加工し、データを保存する。")
    }

    fun postEndTime(endHour: Int, endMinute: Int) {
        TODO("Not yet implemented")
    }

    val groupName = MutableLiveData<String?>("null")
}