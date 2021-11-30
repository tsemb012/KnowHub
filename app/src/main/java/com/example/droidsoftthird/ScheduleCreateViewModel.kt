package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScheduleCreateViewModel: ViewModel() {

    var eventName = MutableLiveData<String>()
    var eventComment = MutableLiveData<String>()
    var eventDate = MutableLiveData<String>()
    var eventTime = MutableLiveData<String>()
    var eventLocation = MutableLiveData<String>()
    var eventGroup = MutableLiveData<String>()

}