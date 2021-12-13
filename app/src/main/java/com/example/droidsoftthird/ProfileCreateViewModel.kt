package com.example.droidsoftthird

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidsoftthird.model.UserProfile
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.google.firebase.auth.ktx.userProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileCreateViewModel @Inject constructor(private val repository: BaseRepositoryImpl): ViewModel() {

    private val _userImageUri = MutableLiveData<Uri>(null)
    val userImageUri: LiveData<Uri>
        get() = _userImageUri

    private val _backgroundImageUri = MutableLiveData<Uri>(null)
    val backgroundImageUri: LiveData<Uri>
        get() = _backgroundImageUri

    var userName = MutableLiveData<String>()
    var userIntroduction = MutableLiveData<String>()
    var gender = MutableLiveData<Int>()
    var age = MutableLiveData<Float>(20.0f)

    private val _prefecture_r = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val prefecture_r: LiveData<String>
        get() = _prefecture_r

    private val _city_r = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val city_r: LiveData<String>
        get() = _city_r


    fun postUserImageUri(uri: Uri) {
        _userImageUri.postValue(uri)
    }

    fun postBackgroundImageUri(uri: Uri) {
        _backgroundImageUri.postValue(uri)
    }

    fun postPrefecture(s: String) {
        _prefecture_r.postValue(s)
        Timber.tag("check_postPrefecture").d(s.toString())
    }

    fun postCity(s: String) {
        _city_r.postValue(s)
    }

    private fun isValid(): Boolean {
        return !userName.value.isNullOrBlank()
                && !_prefecture_r.value.isNullOrBlank()
                && !_city_r.value.isNullOrBlank()
    }



    fun createUserProfile() {
        activateProgressBar()
        viewModelScope.launch {
            if(userImageUri.value != null && backgroundImageUri.value != null)  {
                val result1 = async{repository.uploadPhoto(userImageUri.value!!)}.await()
                val result2 = async{repository.uploadPhoto(backgroundImageUri.value!!)}.await()
                    when{
                        result1 is Result.Success && result2 is Result.Success -> {

                            val userImageRef = result1.data.path.plus(IMAGE_SIZE)
                            val userBackgroundRef = result2.data.path.plus(IMAGE_SIZE)

                            val userProfile = UserProfile(
                                userImageRef,
                                userBackgroundRef,
                                userName.value.toString(),
                                userIntroduction.value.toString(),
                                gender.value,
                                age.value?.toInt(),
                                prefecture_r.value.toString(),
                                city_r.value.toString(),
                            )

                            val authProfileUpdates = userProfileChangeRequest {
                                displayName = userName.value.toString()
                                photoUri = userImageUri.value
                            }

                            val result3:Result<Int> = repository.createUserProfile(userProfile)
                            val result4:Result<Int> = repository.updateAuthProfile(authProfileUpdates)


                            when{
                              result3 is Result.Success && result4 is Result.Success -> {
                                  onHomeClicked()
                              } //TODO アップロード成功時の処理を記述する。
                                //TODO アップロード失敗時、CoroutineScopeを終わらせてスコープの外でまとめて表示処理する。
                            }

                        }
                        //else //TODO アップロード失敗時、CoroutineScopeを終わらせてスコープの外でまとめて表示処理する。
                    }
                }
            } //TODO 画像がNullだった場合の対処法も考える。
        }

    val activateProgressBar = MutableLiveData<Event<String>>()
    fun activateProgressBar(){
        activateProgressBar.value = Event("activateProgressBar")
    }

    val navigationToHome = MutableLiveData<Event<String>>()
    fun onHomeClicked(){
        navigationToHome.value = Event("navigation")
    }




    companion object {
        private const val IMAGE_SIZE = "_200x200"
    }



}

