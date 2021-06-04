package com.example.droidsoftthird

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.droidsoftthird.repository.ScheduleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.time.LocalDate

class ApprovedScheduleViewModel @AssistedInject constructor(
    private val repository: ScheduleRepository,
    @Assisted private val groupId:String,
): ViewModel() {

    private val schedules = mutableMapOf<LocalDate, List<Event>>()//TODO ViewModelに移行する。






    private val _navigateToScheduleDetail = MutableLiveData<String?>()
    val navigateToScheduleDetail
        get()=_navigateToScheduleDetail

    fun onScheduleClicked(id:String){
        _navigateToScheduleDetail.value = id
    }

    fun onScheduleDetailNavigated(){
        _navigateToScheduleDetail.value = null//ここの部分は繊維を発生させないよう。必ずNullにする。
    }


    @AssistedFactory
    interface Factory{
        fun create(groupId: String): ApprovedScheduleViewModel
    }
}