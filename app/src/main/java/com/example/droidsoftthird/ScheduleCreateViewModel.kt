package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.util.*

class ScheduleCreateViewModel: ViewModel() {

    companion object {
        private const val NOT_SET_JPN = "未設定"
        private const val NOT_SET_ENG = "Not set"
    }

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
        TODO("選択日時をセット")
    }

    fun setTimePeriod(startTime: Calendar, endTime: Calendar) {
        TODO("開始・終了時間をセット")
    }

}