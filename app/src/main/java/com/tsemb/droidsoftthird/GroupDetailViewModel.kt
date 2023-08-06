package com.tsemb.droidsoftthird

import androidx.lifecycle.*
import com.tsemb.droidsoftthird.model.domain_model.ApiGroup
import com.tsemb.droidsoftthird.model.domain_model.FacilityEnvironment
import com.tsemb.droidsoftthird.model.domain_model.FrequencyBasis
import com.tsemb.droidsoftthird.model.domain_model.GroupType
import com.tsemb.droidsoftthird.model.domain_model.Style
import com.tsemb.droidsoftthird.usecase.GroupUseCase
import com.tsemb.droidsoftthird.usecase.ProfileUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class GroupDetailViewModel @AssistedInject constructor(
    private val groupUseCase: GroupUseCase,
    private val userUseCase: ProfileUseCase,
    @Assisted private val groupId:String,
):ViewModel() {
    //TODO ローカライズも拡張もしにくいひどいコードなので、全面的に書き直す

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> get() = _userId

    private val _groupDetail = MutableLiveData<ApiGroup?>()
    val groupDetail: LiveData<ApiGroup?>
        get() = _groupDetail

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    val isHostGroup get() = groupDetail.value?.hostUserId == userId.value

    val prefectureAndCity: LiveData<String> = groupDetail.map{ group ->
        if (group?.prefecture == null && group?.city == null && group?.isOnline == true) {
            "オンライン"
    } else if (group?.prefecture != null && group?.city != null) {
            "${group?.prefecture}, ${group?.city}"
        } else if (group?.prefecture != null && group?.city == null) {
            "${group?.prefecture}"
        }  else {
            "指定なし"
        }
    }

    val ageRange: LiveData<String> = groupDetail.map{ group ->

            if (group?.minAge != -1 || group?.maxAge != -1) {
                "${group?.minAge} ~ ${group?.maxAge}才"
            } else {
                "指定なし"
            }
    }

    val numberPerson: LiveData<String> = groupDetail.map{ group ->
            if (group?.maxNumberPerson != -1) {
                "最大参加人数 ${group?.maxNumberPerson}人"
            } else {
                "指定なし"
            }
    }

    val basisFrequency: LiveData<String> = groupDetail.map{ group ->
        when (group?.basis) {
            FrequencyBasis.NONE_FREQUENCY_BASIS -> "指定なし"
            FrequencyBasis.DAILY -> "毎日"
            FrequencyBasis.WEEKLY -> "週 ${group.frequency} 回"
            FrequencyBasis.MONTHLY -> "毎 ${group.frequency} 回"
            FrequencyBasis.IRREGULARLY -> "不定期"
            else -> { "未設定"}
        }
    }

    val groupTypeString: LiveData<String> get() = _groupDetail.map { group ->
        when (group?.groupType) {
            GroupType.INDIVIDUAL_TASK -> "個々の課題"
            GroupType.SHARED_GOAL -> "共通の目標"
            GroupType.NONE_GROUP_TYPE -> "指定なし"
            else -> { "未設定"}
        }
    }

    val facilityEnvironmentString: LiveData<String> = _groupDetail.map { group ->
        when (group?.facilityEnvironment) {
            FacilityEnvironment.NONE_FACILITY_ENVIRONMENT -> "指定なし"
            FacilityEnvironment.ONLINE -> "オンライン"
            FacilityEnvironment.CAFE_RESTAURANT -> "カフェ・レストラン"
            FacilityEnvironment.CO_WORKING_SPACE -> "コワーキングスペース"
            FacilityEnvironment.LIBRARY -> "図書館"
            FacilityEnvironment.PAID_STUDY_SPACE -> "有料学習スペース"
            FacilityEnvironment.PARK -> "公園"
            FacilityEnvironment.RENTAL_SPACE -> "レンタルスペース"
            FacilityEnvironment.OTHER_FACILITY_ENVIRONMENT -> "その他"
            else -> { "指定なし"}
        }
    }

    val styleString: LiveData<String> = _groupDetail.map { group ->
        when (group?.style) {
            Style.FOCUS -> "静かに集中"
            Style.ENJOY -> "楽しくワイワイ"
            Style.NONE_STYLE -> "指定なし"
            else -> { "指定なし"}
        }
    }

    private val _navigateToMyPage = MutableLiveData<String?>()
    val navigateToMyPage
        get()=_navigateToMyPage


    init {
        viewModelScope.launch {
            runCatching { userUseCase.fetchUserId() }
                .onSuccess { _userId.postValue(it) }
                .onFailure { throw it }

            runCatching { groupUseCase.fetchGroupDetail(groupId) }
                .onSuccess { _groupDetail.postValue(it) }
                .onFailure { throw it }
        }
    }

    fun userJoinGroup() {
        viewModelScope.launch {
            runCatching {
                groupUseCase.userJoinGroup(groupId)
            }.onSuccess {
                _message.value = "コミュニティに参加しました"
                _navigateToMyPage.value = "fromHome"
            }.onFailure {
                _message.value = it.message
            }
        }
    }

    fun userLeaveGroup() {
        viewModelScope.launch {
            runCatching {
                groupUseCase.userLeaveGroup(groupId)
            }.onSuccess {
                _message.value = "コミュニティを抜けました"
                _navigateToMyPage.value = "fromMyPage"
            }.onFailure {
                _message.value = it.message
            }
        }
    }

    val confirmJoin = MutableLiveData<Event<String>>()
    fun confirmJoin(){
        confirmJoin.value = Event("activateProgressBar")
    }

    val confirmLeave = MutableLiveData<Event<String>>()
    fun confirmLeave(){
        confirmLeave.value = Event("activateProgressBar")
    }

    fun onMyPageNavigated() {
        _navigateToMyPage.value = null
    }

    @AssistedFactory
    interface Factory{
        fun create(groupId: String): GroupDetailViewModel
    }
}
