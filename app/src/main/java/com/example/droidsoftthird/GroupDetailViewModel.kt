package com.example.droidsoftthird

import androidx.lifecycle.*
import com.example.droidsoftthird.model.domain_model.ApiGroupDetail
import com.example.droidsoftthird.usecase.GroupUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class GroupDetailViewModel @AssistedInject constructor(
    private val useCase: GroupUseCase,
    @Assisted private val groupId:String,
    ):ViewModel() {

    private val _groupDetail = MutableLiveData<ApiGroupDetail?>()
    val groupDetail: LiveData<ApiGroupDetail?>
        get() = _groupDetail

    val prefectureAndCity: LiveData<String> = groupDetail.map{ group ->
        if (group?.prefecture != "未設定" ) {
            "${group?.prefecture}, ${group?.city}"
        } else {
            "未設定"
        }
    }

    val ageRange: LiveData<String> = groupDetail.map{ group ->

            if (group?.minAge != -1 || group?.maxAge != -1) {
                "${group?.minAge} ~ ${group?.maxAge}才"
            } else {
                "未設定"
            }
    }

    val numberPerson: LiveData<String> = groupDetail.map{ group -> //TODO DST-520 消す

            if (group?.minNumberPerson != -1 || group?.maxNumberPerson != -1) {
                "${group?.minNumberPerson} ~ ${group?.maxNumberPerson}人"
            } else {
                "未設定"
            }
    }

    val basisFrequency: LiveData<String> = groupDetail.map{ group ->
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
            runCatching { useCase.fetchGroupDetail(groupId)
            }.onSuccess {
                _groupDetail.postValue(it)
            }.onFailure {
                throw it
            }
        }
    }

    fun userJoinGroup() {
        viewModelScope.launch {
            runCatching {
                useCase.userJoinGroup(groupId)
            }.onSuccess {
                _navigateToMyPage.value = " "
                //TODO ユーザー追加のトーストを出す
            }.onFailure {
                //TODO ユーザー追加失敗のトーストを出す
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