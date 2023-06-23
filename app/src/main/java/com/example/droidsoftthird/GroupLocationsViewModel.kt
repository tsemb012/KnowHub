package com.example.droidsoftthird

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.model.domain_model.AreaCategory
import com.example.droidsoftthird.model.presentation_model.GroupLocationsUiModel
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.example.droidsoftthird.usecase.GroupUseCase
import com.example.droidsoftthird.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GroupLocationsViewModel @Inject constructor(private val useCase: GroupUseCase) : ViewModel() {

    private val _groupCountByArea by lazy { MutableLiveData<LoadState>(LoadState.Initialized) }
    private val _groupsBySelectedArea by lazy { MutableLiveData<Flow<PagingData<ApiGroup>>>(emptyFlow()) }

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
    fun getCountByArea() {
        val job = viewModelScope.launch {
            runCatching { useCase.fetchCountByArea() }
                .onSuccess {
                    _groupCountByArea.value = LoadState.Loaded(it)
                }
                .onFailure {
                    _groupCountByArea.value = LoadState.Error(it)
                }
        }
        _groupCountByArea.value = LoadState.Loading(job)
        job.start()
    }

    fun getGroupsByArea(code: Int, type: AreaCategory) {
        viewModelScope.launch {
            useCase.fetchGroups(ApiGroup.FilterCondition(code, type))
                .cachedIn(viewModelScope)
                .let { _groupsBySelectedArea.value = it }
        }
    }
}