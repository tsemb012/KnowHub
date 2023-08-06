package com.tsemb.droidsoftthird

import androidx.lifecycle.*
import com.tsemb.droidsoftthird.repository.BaseRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: BaseRepositoryImpl): ViewModel() {

    //TODO ログイン時の認証周りの確認機構を設定する。
}