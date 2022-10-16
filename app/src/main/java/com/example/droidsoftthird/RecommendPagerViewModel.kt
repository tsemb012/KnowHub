package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendPagerViewModel @Inject constructor(private val repository: BaseRepositoryImpl):ViewModel() {

    private val _groups = MutableLiveData<List<ApiGroup>?>()
    val groups: LiveData<List<ApiGroup>?>
        get() = _groups

    fun initialize() {
        clearGroups()
        viewModelScope.launch {
            runCatching {
                repository.fetchGroups(0)
            }.onSuccess {
                _groups.postValue(it)
            }.onFailure {
                throw it
            }
        }
    }

    private fun clearGroups() {
        _groups.value = listOf()
    }


    private val _navigateToGroupDetail = MutableLiveData<String?>()
    val navigateToGroupDetail
        get()=_navigateToGroupDetail

    fun onGroupClicked(id:String){
        _navigateToGroupDetail.value = id
    }

    fun onGroupDetailNavigated(){
        _navigateToGroupDetail.value = null
    }

    fun loadMore(currentPage: Int) {
        val nextPage = currentPage.inc()
        viewModelScope.launch {
            runCatching {
                repository.fetchGroups(nextPage)
            }.onSuccess { nextGroups ->
                groups.value?.let { currentGroups ->
                    _groups.postValue(currentGroups + nextGroups)
                }
            }.onFailure {
                Result.Failure(Exception("Network request failed"))
            }
        }
    }

}

