package com.tsemb.droidsoftthird

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsemb.droidsoftthird.model.presentation_model.LoadState
import com.tsemb.droidsoftthird.usecase.GroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(private val useCase: GroupUseCase): ViewModel() {

    private val _groupsLoadState = MutableLiveData<LoadState>()//List<ApiGroup>?>()
    val groupsLoadState: LiveData<LoadState>//List<ApiGroup>?
        get() = _groupsLoadState

    fun getMyGroups() {
        val job = viewModelScope.launch {
            runCatching { useCase.fetchJoinedGroups() }
                .onSuccess {
                    _groupsLoadState.value = LoadState.Loaded(it)
                }
                .onFailure {
                    _groupsLoadState.value = LoadState.Error(it)
                }
        }
        _groupsLoadState.value = LoadState.Loading(job)
        job.start()
    }
}
