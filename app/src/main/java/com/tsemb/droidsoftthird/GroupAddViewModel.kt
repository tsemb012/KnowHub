package com.tsemb.droidsoftthird


import android.net.Uri
import androidx.lifecycle.*
import com.tsemb.droidsoftthird.model.domain_model.EditedGroup
import com.tsemb.droidsoftthird.model.domain_model.FacilityEnvironment
import com.tsemb.droidsoftthird.model.domain_model.FrequencyBasis
import com.tsemb.droidsoftthird.model.domain_model.GroupType
import com.tsemb.droidsoftthird.model.domain_model.Style
import com.tsemb.droidsoftthird.usecase.GroupUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import javax.inject.Inject

@HiltViewModel
class GroupAddViewModel @Inject constructor(private val useCase: GroupUseCase): ViewModel() {

    private val _imageUri = MutableLiveData<Uri>(null)
    val imageUri: LiveData<Uri>
        get() = _imageUri

    var groupName = MutableLiveData<String>()

    val groupIntroduction = MutableLiveData<String>()

    private val _groupType = MutableLiveData(GroupType.NONE_GROUP_TYPE)
    val groupTypeStringId: LiveData<Int> get() = _groupType.map { it.displayNameId }
    val groupType: LiveData<GroupType> get() = _groupType

    private val _style = MutableLiveData(Style.NONE_STYLE)
    val styleStringId: LiveData<Int> get() = _style.map { it.displayNameId }
    val style: LiveData<Style> get() = _style

    private val _prefecture = MutableLiveData("未設定")
    val prefecture: LiveData<String> get() = _prefecture

    private val _city = MutableLiveData("未設定")
    val city: LiveData<String> get() = _city

    private var areaCodes: Pair<Int?, Int?>? = null
    val isOnline: Boolean get() =  areaCodes?.first == 0 && areaCodes?.second == 0

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _facilityEnvironment = MutableLiveData(FacilityEnvironment.NONE_FACILITY_ENVIRONMENT)
    private val facilityEnvironment: LiveData<FacilityEnvironment> get() = _facilityEnvironment
    val facilityEnvironmentStringId: LiveData<Int> get() = _facilityEnvironment.map { it.displayNameId }

    private val _frequencyBasis = MutableLiveData(FrequencyBasis.NONE_FREQUENCY_BASIS)
    private val frequencyBasis: LiveData<FrequencyBasis> get() = _frequencyBasis
    val frequencyBasisStringId: LiveData<Int> get() = _frequencyBasis.map { it.displayNameId }

    private val _frequency = MutableLiveData(-1)
    val frequency: LiveData<Int>
        get() = _frequency

    private val _minAge = MutableLiveData(18)
    val minAge: LiveData<Int>
        get() = _minAge

    private val _maxAge = MutableLiveData(100)
    val maxAge: LiveData<Int>
        get() = _maxAge

    private val _maxNumberPerson = MutableLiveData(10)
    val maxNumberPerson: LiveData<Int>
        get() = _maxNumberPerson

    private val _isChecked = MutableLiveData(false)
    val isChecked: LiveData<Boolean>
        get() = _isChecked

    private fun isValid(): Boolean {
        return !groupName.value.isNullOrBlank() && !groupIntroduction.value.isNullOrBlank() && imageUri.value != null
    }

    val enableState = MediatorLiveData<Boolean>().also { result ->
        result.addSource(groupName) { result.value = isValid() }
        result.addSource(groupIntroduction) { result.value = isValid() }
        result.addSource(imageUri) { result.value = isValid() }
    }

    fun postImageUri(uri: Uri) { _imageUri.postValue(uri) }
    fun postGroupType(type: GroupType) { _groupType.postValue(type) }
    fun postStyle(style: Style) { _style.postValue(style) }
    fun postPrefecture(s: String) { _prefecture.postValue(s) }
    fun postCity(s: String) { _city.postValue(s) }
    fun postFacilityEnvironment(facilityEnvironment: FacilityEnvironment) { _facilityEnvironment.postValue(facilityEnvironment) }
    fun postBasis(frequencyBasis: FrequencyBasis) { _frequencyBasis.postValue(frequencyBasis) }
    fun postFrequency(i: Int) { _frequency.postValue(i) }
    fun postMinAge(i: Int) { _minAge.postValue(i) }
    fun postMaxAge(i: Int) { _maxAge.postValue(i) }
    fun postMaxNumberPerson(i: Int) { _maxNumberPerson.postValue(i) }
    fun postIsChecked(boolean: Boolean) { _isChecked.postValue(boolean) }

    fun createGroup() {
        activateProgressBar()
        viewModelScope.launch {
            if(imageUri.value != null) {
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
                                groupType.value ?: GroupType.NONE_GROUP_TYPE,
                                areaCodes?.first ?: -1,
                                areaCodes?.second ?: -1,
                                isOnline,
                                facilityEnvironment.value ?: FacilityEnvironment.NONE_FACILITY_ENVIRONMENT,
                                style.value ?: Style.NONE_STYLE,
                                frequencyBasis.value ?: FrequencyBasis.NONE_FREQUENCY_BASIS,
                                frequency.value ?: -1,
                                minAge.value ?: 18,
                                maxAge.value ?: 100,
                                maxNumberPerson.value ?: 10,
                                isChecked.value ?: false
                            )
                            runCatching { useCase.createGroup(group) }
                                .onSuccess { onHomeClicked() }
                                .onFailure { _errorMessage.postValue("コミュニティの保存に失敗しました。") }
                        }
                        is Result.Failure -> { _errorMessage.postValue("画像の保存に失敗しました。") }
                    }
                }
            }else{ throw IllegalStateException() }
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
        private const val IMAGE_SIZE = "_400x400"
    }
}










