package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScheduleCreateViewModel: ViewModel() {

    var eventName: LiveData<String> = MutableLiveData()


}