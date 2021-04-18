package com.example.droidsoftthird


import android.net.Uri
import androidx.lifecycle.*
import com.example.droidsoftthird.model.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch


class AddGroupViewModel(val repository: GroupRepository): ViewModel() {

    //TODO ResourceProvider/ApplicationClass/Hilt等を用いて、ViewModelないでR.stringを使用する方法を検討する。
    //TODO より良いUIを検討する。


    private val _imageUri = MutableLiveData<Uri>(null)
    val imageUri: LiveData<Uri>
        get() = _imageUri

    val groupName = MutableLiveData<String>()
    //R.string.please_input_group_name

    val groupIntroduction =
        MutableLiveData<String>("グループの紹介文を記入してください。")
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

    val enableState = MediatorLiveData<Boolean>().also { result ->
        result.addSource(groupName) { result.value = isValid() }
    }


    fun postImageUri(uri: Uri) {
        _imageUri.postValue(uri)
    }

    fun postGroupType(s: String) {
        _groupType.postValue(s)
    }

    fun postPrefecture(s: String) {
        _prefecture.postValue(s)
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

    private fun isValid(): Boolean {
        return !groupName.value.isNullOrBlank() && !groupIntroduction.value.isNullOrBlank()
        //TODO UIを研究し、Userにとってより使いやすい手法を探る。
    }


    fun createGroup() {
        //TODO 画像がNullだった場合の対処法も考える。

        viewModelScope.launch {
            if(imageUri.value != null) {
                val result: Result<StorageReference> = repository.uploadPhoto(imageUri.value!!).also {
                    when(it){
                        is Result.Success -> {
                            val storageRef = it.data.toString()
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
                        }
                    //else ->
                    }
                }
                    /*              is Result.Success -> _groups.postValue(result.data)
                else //TODO SnackBarを出現させる処理を記入する。*/


            }
        }
    }

}

    //TODO DroidSecondのuploadFromUriを用いて、Repository経由でアップロード処理を行う。

/*    private void uploadFromUri(Uri fileUri) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());
        showProgressBar(getString(R.string.progress_uploading));
        mFileUri = fileUri;//ファイルパス保存
        photoRef = mStorageRef.child("photos").child(fileUri.getLastPathSegment());
        photoRef.putFile(fileUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getActivity(), R.string.upload_fail, Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getActivity(), R.string.upload_success, Toast.LENGTH_SHORT).show();
                downloadFromPath();
                hideProgressBar();
            }

        });
    }*/







