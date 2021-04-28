package com.example.droidsoftthird

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.Group
import com.example.droidsoftthird.repository.UserGroupRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class MyPageViewModel @ViewModelInject constructor(private val repository: UserGroupRepository): ViewModel() {

    private val _groups = MutableLiveData<List<Group>?>()
    val groups: LiveData<List<Group>?>
        get() = _groups

    fun getAllGroups(){
        viewModelScope.launch {
            val result = try{
                repository.getAllGroups()
            } catch(e: Exception){
                Result.Error(Exception("Network request failed"))
            }
            when (result) {
                is Result.Success -> _groups.postValue(result.data)
                //else //TODO SnackBarを出現させる処理を記入する。
            }
            Timber.tag("check_result1").d(result.toString())
        }
    }

    private val _navigateToGroupDetail = MutableLiveData<String>()
    val navigateToGroupDetail
        get()=_navigateToGroupDetail

    fun onGroupClicked(id:String){
        _navigateToGroupDetail.value = id
    }

    fun onGroupDetailNavigated(){
        _navigateToGroupDetail.value = " "
    }
}
