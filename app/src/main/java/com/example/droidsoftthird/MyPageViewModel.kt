package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(private val repository: BaseRepositoryImpl): ViewModel() {

    private val _groups = MutableLiveData<List<ApiGroup>?>()
    val groups: LiveData<List<ApiGroup>?>
        get() = _groups

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun getMyGroups() {
        viewModelScope.launch {
            runCatching { repository.fetchJoinedGroups() }
                .onSuccess { _groups.value = it }
                .onFailure { _message.value = it.message }
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
