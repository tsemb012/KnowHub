package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.droidsoftthird.model.domain_model.ApiGroup
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

    private val error = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?>
        get() = error


    fun initialize() {
        loadGroups()
    }

    fun cancel() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            useCase.fetchGroups()
                .cachedIn(viewModelScope)
                .catch { e ->
                    error.value = e.message
                }
                .collect {
                    _groupsFlow.value = it
                }
        }
    }
}

