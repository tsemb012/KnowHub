package com.example.droidsoftthird

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.droidsoftthird.model.rails_model.UserDetail
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import javax.inject.Inject


class ProfileViewModel @Inject constructor(private val repository: BaseRepositoryImpl): ViewModel() {

    //TODO 基本的に個人情報はRailsAPIにしまう。ユーザー名とユーザー画像だけはFirebaseとRailsAPIどちらにも入れる。

    private val _userDetail = MutableLiveData<UserDetail?>()
    val userDetail: LiveData<UserDetail?>
        get() = _userDetail

    private val _message = MutableLiveData<ProfileMessage?>()
    val message: LiveData<ProfileMessage?>
        get() = _message


    private fun fetchUserDetail(){
        //TODO ユーザー情報を取得する
    }

    inner class ProfileMessage {

    }

}


