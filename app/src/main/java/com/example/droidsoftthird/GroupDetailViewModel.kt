package com.example.droidsoftthird

import androidx.lifecycle.*
import com.example.droidsoftthird.model.domain_model.*
import com.example.droidsoftthird.usecase.GroupUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class GroupDetailViewModel @AssistedInject constructor(
    private val useCase: GroupUseCase,
    @Assisted private val groupId:String,
):ViewModel() {
    //TODO ローカライズも拡張もしにくいひどいコードなので、全面的に書き直す

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

    val numberPerson: LiveData<String> = groupDetail.map{ group ->
            if (group?.maxNumberPerson != -1) {
                "最大参加人数 ${group?.maxNumberPerson}人"
            } else {
                "未設定"
            }
    }

    val basisFrequency: LiveData<String> = groupDetail.map{ group ->
        when (group?.basis) {
            FrequencyBasis.NONE_FREQUENCY_BASIS -> "未設定"
            FrequencyBasis.DAILY -> "毎日"
            FrequencyBasis.WEEKLY -> "週 ${group.frequency} 回"
            FrequencyBasis.MONTHLY -> "毎 ${group.frequency} 回"
            else -> { "未設定"}
        }
    }

    val groupTypeString: LiveData<String> get() = _groupDetail.map { group ->
        when (group?.groupType) {
            GroupType.SEMINAR -> "セミナー"
            GroupType.WORKSHOP -> "ワークショップ"
            GroupType.MOKUMOKU -> "もくもく会"
            GroupType.OTHER_GROUP_TYPE -> "その他"
            GroupType.NONE_GROUP_TYPE -> "未設定"
            else -> { "未設定"}
        }
    }

    val facilityEnvironmentString: LiveData<String> = _groupDetail.map { group ->
        when (group?.facilityEnvironment) {
            FacilityEnvironment.NONE_FACILITY_ENVIRONMENT -> "未設定"
            FacilityEnvironment.ONLINE -> "オンライン"
            FacilityEnvironment.CAFE_RESTAURANT -> "カフェ・レストラン"
            FacilityEnvironment.CO_WORKING_SPACE -> "コワーキングスペース"
            FacilityEnvironment.LIBRARY -> "図書館"
            FacilityEnvironment.PAID_STUDY_SPACE -> "有料学習スペース"
            FacilityEnvironment.PARK -> "公園"
            FacilityEnvironment.RENTAL_SPACE -> "レンタルスペース"
            FacilityEnvironment.OTHER_FACILITY_ENVIRONMENT -> "その他"
            else -> { "未設定"}
        }
    }

    private val _navigateToMyPage = MutableLiveData<String?>()
    val navigateToMyPage
        get()=_navigateToMyPage


    init {
        viewModelScope.launch {
            runCatching { useCase.fetchGroupDetail(groupId) }.
            onSuccess { _groupDetail.postValue(it) }.
            onFailure { throw it }
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