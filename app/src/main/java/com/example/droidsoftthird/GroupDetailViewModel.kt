package com.example.droidsoftthird

import androidx.lifecycle.*
import com.example.droidsoftthird.model.Group
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class GroupDetailViewModel @AssistedInject constructor(
    private val repository: BaseRepositoryImpl<Any?>,
    @Assisted private val groupId:String,
    ):ViewModel() {

    private val _group = MutableLiveData<Group?>()
    val group: LiveData<Group?>
        get() = _group

    val prefectureAndCity: LiveData<String> = Transformations.map(group){ group ->
        if (group?.prefecture != "未設定" ) {
            "${group?.prefecture}, ${group?.city}"
        } else {
            "未設定"
        }
    }


    val ageRange: LiveData<String> = Transformations.map(group) { group ->

            if (group?.minAge != -1 || group?.maxAge != -1) {
                "${group?.minAge} ~ ${group?.maxAge}才"
            } else {
                "未設定"
            }
    }

    val numberPerson: LiveData<String> = Transformations.map(group) { group ->

            if (group?.minNumberPerson != -1 || group?.maxNumberPerson != -1) {
                "${group?.minNumberPerson} ~ ${group?.maxNumberPerson}人"
            } else {
                "未設定"
            }
    }

    val basisFrequency: LiveData<String> = Transformations.map(group){ group ->
        if (group?.basis != "未設定" ) {
            "${group?.basis}${group?.frequency}回"
        } else {
            "未設定"
        }
    }

    private val _navigateToMyPage = MutableLiveData<String?>()
    val navigateToMyPage
        get()=_navigateToMyPage


    init {
        viewModelScope.launch {
            val result = try{
                repository.getGroup(groupId)
            } catch(e: Exception){
                Result.Failure(Exception("Network request failed"))
            }
            when (result) {
                is Result.Success -> _group.postValue(result.data)
                //else //TODO SnackBarを出現させる処理を記入する。
            }
            Timber.tag("check_result1").d(result.toString())
        }
    }

    fun userJoinGroup() {
        viewModelScope.launch {
            _navigateToMyPage.value = " "
            val result = try {
                repository.userJoinGroup(groupId)
            } catch (e: Exception) {
                Result.Failure(Exception("Network request failed"))
            }
            when (result) {
                is Result.Success -> Timber.tag(TAG.plus("1")).d(result.data.toString())
                //TODO 成功時の処理を行う。
                is Result.Failure ->  Timber.tag(TAG.plus("2")).d(result.exception.toString())
                //TODO SnackBarを出現させる処理を記入する。*/
            }
        }
    }

    val confirmJoin = MutableLiveData<Event<String>>()
    fun confirmJoin(){
        confirmJoin.value = Event("activateProgressBar")
    }

    fun onMyPageNavigated() {
        _navigateToMyPage.value = null
    }

    //TODO onClick時のロジック処理を受け持つ。

    @AssistedFactory
    interface Factory{
        fun create(groupId: String): GroupDetailViewModel
    }

    companion object {
        private val TAG: String? = GroupDetailViewModel::class.simpleName
    }

}