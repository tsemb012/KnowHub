package com.example.droidsoftthird.ui.myPage

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.QueryType
import com.example.droidsoftthird.Result
import com.example.droidsoftthird.model.Group
import com.example.droidsoftthird.repository.UserGroupRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class MyPageViewModel @ViewModelInject constructor(private val repository: UserGroupRepository): ViewModel() {

    private val _groups = MutableLiveData<List<Group>?>()
    val groups: LiveData<List<Group>?>
        get() = _groups

    fun getMyGroups(){
        viewModelScope.launch {
            val result = try{
                repository.getGroups(QueryType.MY_PAGE.value)

            } catch(e: Exception){
                Result.Error(Exception("Network request failed"))
            }
            Timber.tag("check_result1-3").d(result.toString())
            when (result) {
                is Result.Success -> _groups.postValue(result.data)
                //else //TODO SnackBarを出現させる処理を記入する。
            }
            Timber.tag("check_result1").d(result.toString())
        }
    }

    private val _navigateToChatRoom = MutableLiveData<String?>()
    val navigateToChatRoom
        get()=_navigateToChatRoom

    fun onGroupClicked(id:String){
        _navigateToChatRoom.value = id
    }

    fun onChatRoomNavigated(){
        _navigateToChatRoom.value = null//ここの部分は繊維を発生させないよう。必ずNullにする。
    }
}
