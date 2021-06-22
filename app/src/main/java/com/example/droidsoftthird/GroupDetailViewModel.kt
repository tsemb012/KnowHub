package com.example.droidsoftthird

import androidx.lifecycle.*
import com.example.droidsoftthird.model.Group
import com.example.droidsoftthird.repository.UserGroupRepository
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

    private val _group = MutableLiveData<Group?>()
    val group: LiveData<Group?>
        get() = _group

    private val _navigateToMyPage = MutableLiveData<String?>()
    val navigateToMyPage
        get()=_navigateToMyPage


    init {
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

    fun userJoinGroup() {
        viewModelScope.launch {
            _navigateToMyPage.value = " "
            val result = try {
                repository.userJoinGroup(groupId,group.value.groupName)
            } catch (e: Exception) {
                Result.Error(Exception("Network request failed"))
            }
            when (result) {
                is Result.Success -> Timber.tag(TAG.plus("1")).d(result.data.toString())
                //TODO 成功時の処理を行う。
                is Result.Error ->  Timber.tag(TAG.plus("2")).d(result.exception.toString())
                //TODO SnackBarを出現させる処理を記入する。*/
            }
        }
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