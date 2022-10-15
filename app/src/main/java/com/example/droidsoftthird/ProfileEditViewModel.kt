package com.example.droidsoftthird

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.Area
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.fire_model.LoadState
import com.example.droidsoftthird.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(private val UseCase: ProfileUseCase): ViewModel( ) {

    private val _loadState by lazy { MutableLiveData<LoadState>() }
    private val _rawUserDetail by lazy { MutableLiveData<UserDetail>() }
    private val _editedUserDetail by lazy { MutableLiveData<UserDetail>() }
    private val _name = MutableLiveData<String>()
    private val _comment = MutableLiveData<String>()
    val _userImage: MutableLiveData<String> = MutableLiveData()
    val _backgroundImage: MutableLiveData<String> = MutableLiveData()
    private val _age: MutableLiveData<Int> by lazy { MutableLiveData() }
    private val _area: MutableLiveData<Area> by lazy { MutableLiveData() }//TODO validationをどうつけるか検討する。
    private val _gender: MutableLiveData<String> by lazy { MutableLiveData() }

    val uiModel by lazy {
        combine(
                ProfileUiModel(),
                _rawUserDetail,
                _name,
                _comment,
                _userImage,
                _backgroundImage,
                _age,
                _area,
                _gender
        ) { current, _name, _comment, _userImage, _backgroundImage, _rawUserDetail, _age, _area, _gender ->
            ProfileUiModel(current, _name, _comment, _userImage, _backgroundImage, _rawUserDetail, _age, _area, _gender)
        }
    }

    fun postAge(age: Int) { _age.value = age }
    fun postArea(area: Area) { _area.value = area }
    fun postGender(gender: UserDetail.Gender) { _gender.value = gender.name.lowercase() }

    fun fetchUserDetail() {
        val job = viewModelScope.launch {
            runCatching { UseCase.fetchUserDetail() }
                .onSuccess {
                    _rawUserDetail.value = it
                    _loadState.value = LoadState.Loaded(it)
                }
                .onFailure { _loadState.value = LoadState.Error(it) }
        }
        _loadState.value = LoadState.Loading(job)
        job.start()
    }

    fun submitProfile( TODO("userDetail") ) {
        profileUseCase.
    }

    //TODO 画面遷移を作成。
    //TODO userDetailを作成。

}
