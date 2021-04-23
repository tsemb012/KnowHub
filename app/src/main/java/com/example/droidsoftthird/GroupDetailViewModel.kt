package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.Group
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class GroupDetailViewModel(private val groupId:String , private val repository: UserGroupRepository):ViewModel() {

    private val _group = MutableLiveData<Group>()
    val group: LiveData<Group>
        get() = _group

    fun getGroup() {
        viewModelScope.launch {
            val result = try{
                repository.getGroup(groupId)
            } catch(e: Exception){
                Result.Error(Exception("Network request failed"))
            }
            when (result) {
                is Result.Success -> _group.postValue(result.data)
                //else //TODO SnackBarを出現させる処理を記入する。
            }
            Timber.tag("check_result1").d(result.toString())
        }
    }

    //TODO onClick時のロジック処理を受け持つ。

}