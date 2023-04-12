package com.example.droidsoftthird


import android.net.Uri
import androidx.lifecycle.*
import com.example.droidsoftthird.model.domain_model.EditedGroup
import com.example.droidsoftthird.usecase.GroupUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

@HiltViewModel
class GroupAddViewModel @Inject constructor(private val useCase: GroupUseCase): ViewModel() {

    //TODO ResourceProvider/ApplicationClass/Hilt等を用いて、ViewModelないでR.stringを使用する方法を検討する。
    private val _imageUri = MutableLiveData<Uri>(null)
    val imageUri: LiveData<Uri>
        get() = _imageUri

    var groupName = MutableLiveData<String>()

    val groupIntroduction = MutableLiveData<String>()

    private val _groupType = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val groupType: LiveData<String>
        get() = _groupType

    private val _prefecture = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val prefecture: LiveData<String>
        get() = _prefecture

    private val _city = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val city: LiveData<String>
        get() = _city

    private var areaCodes: Pair<Int?, Int?>? = null

    private val _facilityEnvironment = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val facilityEnvironment: LiveData<String>
        get() = _facilityEnvironment

    private val _basis = MutableLiveData<String>("未設定")//R.string.no_set.toString()
    val basis: LiveData<String>
        get() = _basis

    private val _frequency = MutableLiveData<Int>(-1)//R.string.no_set.toString()
    val frequency: LiveData<Int>
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

    fun postFrequency(i: Int) {
        _frequency.postValue(i)
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
        activateProgressBar()
        viewModelScope.launch {
            if(imageUri.value != null) {//TODO 画像の処理の仕方を再検討する。
                async{ useCase.uploadPhoto(imageUri.value!!) }.await().also { it ->
                    when(it){
                        is Result.Success -> {
                            val storageRef = it.data.path.plus(IMAGE_SIZE)//FirebaseExtinctionで画像加工及びファイル名を変更ししているため、ファイル名を修正する。
                            val group = EditedGroup(
                                null,
                                FirebaseAuth.getInstance().uid ?: throw IllegalStateException(),
                                storageRef,
                                groupName.value.toString(),
                                groupIntroduction.value.toString(),
                                groupType.value.toString(),
                                areaCodes?.first ?: -1,
                                areaCodes?.second ?: -1,
                                facilityEnvironment.value.toString(),
                                basis.value.toString(),
                                frequency.value!!,
                                minAge.value!!,
                                maxAge.value!!,
                                minNumberPerson.value!!,
                                maxNumberPerson.value!!,
                                isChecked.value!!
                            )
                            runCatching { useCase.createGroup(group) }
                                .onSuccess { onHomeClicked() }
                                .onFailure { throw it }
                        }
                        is Result.Failure -> { TODO("アップロード失敗時の処理を記述する。") }
                    }
                }
            }else{ TODO("画像処理がNullだった場合の処理を検討する。") }
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

    fun postCodes(pair: Pair<Int?, Int?>) {
        areaCodes = pair
    }

    companion object {
        private const val IMAGE_SIZE = "_200x200"
    }


}










