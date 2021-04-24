package com.example.droidsoftthird

import androidx.lifecycle.*
import com.example.droidsoftthird.model.Group
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class GroupDetailViewModel @AssistedInject constructor(
    private val repository: UserGroupRepository,
    @Assisted private val groupId:String,
    ):ViewModel() {

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

    @AssistedFactory
    interface Factory{
        fun create(groupId: String): GroupDetailViewModel
    }

}