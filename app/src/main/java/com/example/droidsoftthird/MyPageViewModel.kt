package com.example.droidsoftthird

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.Group
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class MyPageViewModel @ViewModelInject constructor(private val repository: BaseRepositoryImpl): ViewModel() {

    private val _groups = MutableLiveData<List<Group>?>()
    val groups: LiveData<List<Group>?>
        get() = _groups

    fun getMyGroups(){
        viewModelScope.launch {
            val result = try{
                repository.getGroups(GroupQuery.MY_PAGE.value)

            } catch(e: Exception){
                Result.Failure(Exception("Network request failed"))
            }
            Timber.tag("check_result1-3").d(result.toString())
            when (result) {
                is Result.Success -> _groups.postValue(result.data)
                //else //TODO SnackBarを出現させる処理を記入する。
            }
            Timber.tag("check_result1").d(result.toString())
        }
    }

    private val _navigateToChatRoom = MutableLiveData<Pair<String,String>?>()
    val navigateToChatRoom
        get()=_navigateToChatRoom

    fun onGroupClicked(groupId:String, groupName:String){

        _navigateToChatRoom.value = groupId to groupName
    }

    fun onChatRoomNavigated(){
        _navigateToChatRoom.value = null//ここの部分は繊維を発生させないよう。必ずNullにする。
    }
}
