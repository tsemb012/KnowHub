package com.example.droidsoftthird
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.*
import com.example.droidsoftthird.model.Schedule
import com.example.droidsoftthird.repository.ScheduleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.time.LocalDate


class ApprovedScheduleViewModel @AssistedInject constructor(
    private val repository: ScheduleRepository,
    @Assisted private var groupId:String = "all",//TODO なんとか初期値を設定する。
): ViewModel() {

    private val _allSchedules = MutableLiveData<List<Schedule>?>()
    private val allSchedules: LiveData<List<Schedule>?>
        get() = _allSchedules

    private val _schedules = MutableLiveData<List<Schedule>>()
    val schedules: LiveData<List<Schedule>>
        get() = _schedules

    private val _selectedDate = MutableLiveData<LocalDate?>(null)
    val selectedDate: LiveData<LocalDate?>
        get() = _selectedDate

    private val _oldDate = MutableLiveData<LocalDate?>(null)
    val oldDate: LiveData<LocalDate?>
        get() = _oldDate

    private val _groups = MutableLiveData<List<String>?>()
    val groups: LiveData<List<String>?>
        get() = _groups

    val events: LiveData<MutableMap<LocalDate, MutableList<Schedule>>> =
        Transformations.map(allSchedules) { allSchedules ->
            val events = mutableMapOf<LocalDate, MutableList<Schedule>>()
            allSchedules?.forEach {
                val list = it.date?.let { date -> events.getOrDefault(date,ArrayList<Schedule>()) }
                list?.add(it)
            }
        return@map events
    }

    init {
        getSchedules()
        getGroupArray()
    }

    fun getSchedules(){
        viewModelScope.launch {
            val result = try{
                repository.getSchedules(ScheduleState.APPROVED, groupId)

            } catch(e: Exception){
                Result.Error(Exception("Network request failed"))
            }
            when (result) {
                is Result.Success -> _allSchedules.postValue(result.data)
                //is Result.Error ->
            }
        }
    }

    private fun getGroupArray(){
        viewModelScope.launch {
            val result = try{
                repository.getGroupArray()

            } catch(e: Exception){
                Result.Error(Exception("Network request failed"))
            }
            when (result) {
                is Result.Success -> _groups.postValue(result.data)
                //is Result.Error ->
            }
        }
    }

    fun selectDate(date: LocalDate) {
        if (selectedDate.value != date) {
            _oldDate.postValue(selectedDate.value)
            _selectedDate.postValue(date)
            _schedules.postValue(events.value?.get(date))
        }
    }

    fun onItemSelectedGroup(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //positionを用いて、Mapから当該Keyを抜き出して、groupIdを変更して、getAllSchedulesを呼び出す。
    }

    val navigateToScheduleDetail = MutableLiveData<Event<String>>()
    fun onScheduleClicked(scheduleId:String){
        navigateToScheduleDetail.value = Event(scheduleId)
    }

    @AssistedFactory
    interface Factory{
        fun create(groupId: String): ApprovedScheduleViewModel
    }
}