package com.example.droidsoftthird


import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.droidsoftthird.model.Group
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber


class GroupAddViewModel @ViewModelInject constructor(private val repository: BaseRepositoryImpl): ViewModel() {

    //TODO ResourceProvider/ApplicationClass/Hilt等を用いて、ViewModelないでR.stringを使用する方法を検討する。
    //TODO より良いUIを検討する。



    private val _imageUri = MutableLiveData<Uri>(null)
    val imageUri: LiveData<Uri>
        get() = _imageUri

    var groupName = MutableLiveData<String>()
    //R.string.please_input_group_name

    val groupIntroduction =
        MutableLiveData<String>()
    //R.string.please_input_group_introduction.toString()

    private val _groupType = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val groupType: LiveData<String>
        get() = _groupType

    private val _prefecture = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val prefecture: LiveData<String>
        get() = _prefecture

    private val _city = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val city: LiveData<String>
        get() = _city

    private val _facilityEnvironment = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val facilityEnvironment: LiveData<String>
        get() = _facilityEnvironment

    private val _basis = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val basis: LiveData<String>
        get() = _basis

    private val _frequency = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val frequency: LiveData<String>
        get() = _frequency

    private val _minAge = MutableLiveData<Int>(-1)
    val minAge: LiveData<Int>
        get() = _minAge

    private val _maxAge = MutableLiveData<Int>(-1)
    val maxAge: LiveData<Int>
        get() = _maxAge

    private val _minNumberPerson = MutableLiveData<Int>(-1)
    val minNumberPerson: LiveData<Int>
        get() = _minNumberPerson

    private val _maxNumberPerson = MutableLiveData<Int>(-1)
    val maxNumberPerson: LiveData<Int>
        get() = _maxNumberPerson

    private val _isChecked = MutableLiveData<Boolean>(false)
    val isChecked: LiveData<Boolean>
        get() = _isChecked

    private fun isValid(): Boolean {
        return !groupName.value.isNullOrBlank() && !groupIntroduction.value.isNullOrBlank()
    }

    val enableState = MediatorLiveData<Boolean>().also { result ->
        result.addSource(groupName) { result.value = isValid() }
        result.addSource(groupIntroduction) { result.value = isValid() }
    }


    fun postImageUri(uri: Uri) {
        _imageUri.postValue(uri)
    }

    fun postGroupType(s: String) {
        _groupType.postValue(s)
        Timber.tag("check_postGroupType").d(s.toString())
    }

    fun postPrefecture(s: String) {
        _prefecture.postValue(s)
        Timber.tag("check_postPrefecture").d(s.toString())
    }

    fun postCity(s: String) {
        _city.postValue(s)
    }

    fun postFacilityEnvironment(s: String) {
        _facilityEnvironment.postValue(s)
    }

    fun postBasis(s: String) {
        _basis.postValue(s)
    }

    fun postFrequency(s: String) {
        _frequency.postValue(s)
    }

    fun postMinAge(i: Int) {
        _minAge.postValue(i)

    }

    fun postMaxAge(i: Int) {
        _maxAge.postValue(i)
    }

    fun postMinNumberPerson(i: Int) {
        _minNumberPerson.postValue(i)
    }

    fun postMaxNumberPerson(i: Int) {
        _maxNumberPerson.postValue(i)
    }

    fun postIsChecked(boolean: Boolean) {
        _isChecked.postValue(boolean)
    }




    fun createGroup() {
        //DONE DroidSecondのuploadFromUriを用いて、Repository経由でアップロード処理を行う。
        activateProgressBar()
        viewModelScope.launch {
            if(imageUri.value != null) {
                async{repository.uploadPhoto(imageUri.value!!)}.await().also {
                    when(it){
                        is Result.Success -> {
                            val storageRef = it.data.path.plus(IMAGE_SIZE)//FirebaseExtinctionで画像加工及びファイル名を変更ししているため、ファイル名を修正する。
                            val group = Group(
                                FirebaseAuth.getInstance().uid,
                                storageRef,
                                groupName.value.toString(),
                                groupIntroduction.value.toString(),
                                groupType.value.toString(),
                                prefecture.value.toString(),
                                city.value.toString(),
                                facilityEnvironment.value.toString(),
                                basis.value.toString(),
                                frequency.value.toString(),
                                minAge.value!!,
                                maxAge.value!!,
                                minNumberPerson.value!!,
                                maxNumberPerson.value!!,
                                isChecked.value!!
                            )
                            val result:Result<Int> = repository.createGroup(group)
                            when(result){
                              is Result.Success ->  {onHomeClicked()}//TODO アップロード成功時の処理を記述する。
                              //TODO アップロード失敗時、CoroutineScopeを終わらせてスコープの外でまとめて表示処理する。
                            }

                        }
                    //else //TODO アップロード失敗時、CoroutineScopeを終わらせてスコープの外でまとめて表示処理する。
                    }
                }
            }else{//TODO 画像がNullだった場合の対処法も考える。
            }
        }
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










