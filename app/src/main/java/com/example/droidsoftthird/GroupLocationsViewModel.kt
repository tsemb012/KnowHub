package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.domain_model.GroupCountByArea
import com.example.droidsoftthird.usecase.GroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GroupLocationsViewModel @Inject constructor(private val useCase: GroupUseCase) : ViewModel() {

    private val _groupCountByArea = MutableLiveData<List<GroupCountByArea>?>()
    val groupCountByArea: LiveData<List<GroupCountByArea>?>
        get() = _groupCountByArea

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun getGroupCountByArea() {
        viewModelScope.launch {
            runCatching { useCase.fetchGroupCountByArea() }
                .onSuccess {
                    _groupCountByArea.value = it }
                .onFailure {
                    _message.value = it.message }
        }
    }




}