package com.example.droidsoftthird

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.presentation_model.GroupLocationsUiModel
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.usecase.GroupUseCase
import com.example.droidsoftthird.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GroupLocationsViewModel @Inject constructor(private val useCase: GroupUseCase) : ViewModel() {

    private val _groupCountByArea by lazy { MutableLiveData<LoadState>(LoadState.Initialized) }
    private val _groupsBySelectedArea by lazy { MutableLiveData<LoadState>(LoadState.Initialized) }

    val uiModel by lazy {
        combine(
            GroupLocationsUiModel(),
            _groupCountByArea,
            _groupsBySelectedArea
        ) { current, _groupCountByArea, _groupsBySelectedArea ->
            GroupLocationsUiModel(
                current = current,
                groupCountByAreaLoadState = _groupCountByArea,
                groupsBySelectedAreaLoadState = _groupsBySelectedArea
            )
        }
    }


    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun getCountByArea() {
        viewModelScope.launch {
            runCatching { useCase.fetchCountByArea() }
                .onSuccess {
                    _groupCountByArea.value = LoadState.Loaded(it)
                }
                .onFailure {
                    _message.value = it.message }
        }
    }

    fun getGroupsByArea(code: Int, type: String) {
        viewModelScope.launch {
            runCatching {
                useCase.fetchGroups(ApiGroup.FilterCondition(code, type)).cachedIn(viewModelScope)
            }
                .onSuccess {
                    _groupsBySelectedArea.value = LoadState.Loaded(it)
                    Log.d("GroupLocationsViewModel", "getGroupsByArea: $it")
                }
                .onFailure {
                    _message.value = it.message
                    Log.d("GroupLocationsViewModel", "getGroupsByArea: $it")
                }
        }
    }
}