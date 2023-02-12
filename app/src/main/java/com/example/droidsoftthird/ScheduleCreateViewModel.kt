package com.example.droidsoftthird

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.droidsoftthird.model.presentation_model.LoadState
import java.time.LocalDate
import java.util.*

class ScheduleCreateViewModel(): ViewModel() {

    companion object {
        private const val NOT_SET_JPN = "未設定"
        private const val NOT_SET_ENG = "Not set"
    }

    val groupsLoadState: MutableLiveData<LoadState> = MutableLiveData(LoadState.Initialized)
    var eventName = MutableLiveData<String>()
    var eventComment = MutableLiveData<String>()

    var eventDate = MutableLiveData<String>()
    //TODO データの持ち方に関しては、
    //TODO GroupAddのところが参考になるはず。そことRPとUiModelを融合させる。
    //TODO　レイアウトについても青と黄色を反転させた方がおしゃれになりそうだけど。
    //Constraintにして上部に説明を入れてあげたほうが全体のレイアウトが綺麗になるのでは？
    var eventTime = MutableLiveData<String>()
    var eventLocation = MutableLiveData<String>()
    var eventGroup = MutableLiveData<String>()

    fun setSelectedDate(selectedDate: LocalDate?) {
        eventDate.value = selectedDate?.toString()?.format("yyyy/MM/dd") ?: NOT_SET_JPN
    }

    fun setTimePeriod(startTime: Calendar, endTime: Calendar) {
        eventTime.value = "${startTime.get(Calendar.HOUR_OF_DAY)}:${startTime.get(Calendar.MINUTE)} - ${endTime.get(Calendar.HOUR_OF_DAY)}:${endTime.get(Calendar.MINUTE)}"
    }

}