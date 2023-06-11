package com.example.droidsoftthird

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.repository.csvloader.CityCsvLoader
import com.example.droidsoftthird.repository.csvloader.PrefectureCsvLoader
import com.example.droidsoftthird.usecase.GroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendGroupsViewModel @Inject constructor(private val useCase: GroupUseCase):ViewModel() {

    private val _groupsFlow = MutableStateFlow<PagingData<ApiGroup>>(PagingData.empty())
    val groupsFlow get() = _groupsFlow

    private val _groupFilterCondition: MutableState<ApiGroup.FilterCondition> = mutableStateOf(ApiGroup.FilterCondition())
    val groupFilterCondition: State<ApiGroup.FilterCondition>
        get() = _groupFilterCondition

    var prefectureList: List<PrefectureCsvLoader.PrefectureLocalItem> = listOf()
    var cityList: List<CityCsvLoader.CityLocalItem> = listOf()

    private val error = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?>
        get() = error
    fun initialize() {
        loadGroups()
        loadLocalArea()
    }

    fun cancel() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    fun updateFilterCondition(filterCondition: ApiGroup.FilterCondition) {
        _groupFilterCondition.value = filterCondition
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            useCase.fetchGroups(groupFilterCondition.value.copy(allowMaxNumberGroupShow = false)) //人数が最大なグループを表示しないようにする。
                .cachedIn(viewModelScope)
                .catch { e ->
                    error.value = e.message
                }
                .collect {
                    _groupsFlow.value = it
                }
        }
    }

    private fun loadLocalArea() {
        viewModelScope.launch {
            prefectureList = useCase.fetchPrefectureList()
            cityList = useCase.fetchCityList()
        }
    }
}

