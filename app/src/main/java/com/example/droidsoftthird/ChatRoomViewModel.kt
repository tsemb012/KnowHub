package com.example.droidsoftthird

import android.net.Uri
import androidx.lifecycle.*
import com.example.droidsoftthird.model.domain_model.ChatGroup
import com.example.droidsoftthird.model.domain_model.fire_model.*
import com.example.droidsoftthird.repository.MessageRepository
import com.example.droidsoftthird.usecase.GroupUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ChatRoomViewModel @AssistedInject constructor(
    private val repository: MessageRepository,
    private val groupUseCase: GroupUseCase,
    @Assisted private val groupId:String
) : ViewModel() {

    private val chatId = (if (BuildConfig.DEBUG) "debug" else "release") + "_" + groupId

    val authUser = FirebaseAuth.getInstance().currentUser

    private val _chatGroup = MutableLiveData<ChatGroup>()
    val chatGroup: LiveData<ChatGroup> get() = _chatGroup

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _notifier = MutableLiveData<Boolean>()
    val notifier: LiveData<Boolean> get() = _notifier

    //=====EditMessageでの入力処理を管理
    val editMessage = MutableLiveData<String>()//双方向バインディングにより、入力されたデータを保存。
    val enableState = MediatorLiveData<Boolean>().also { result ->
        result.addSource(editMessage) { result.value = isValid() }
    }
    private fun isValid(): Boolean {
        return !editMessage.value.isNullOrBlank()
    }

    //======MessageListの受け取り
    private val _messages = MutableLiveData<List<FireMessage>?>()
    val messages: LiveData<List<FireMessage>?>
        get() = _messages

    init {
        viewModelScope.launch {
            runCatching {
                groupUseCase.fetchChatGroup(groupId)
            }.
            onSuccess {
                _chatGroup.value = it
            }.
            onFailure {
                _error.value = it.message
            }
        }

        viewModelScope.launch {
            repository.getChatEvents(chatId).collect{
                val messageList = ArrayList<FireMessage>()
                for(doc in it){
                    val message = when(doc.get("messageType")){
                        0.0 -> {doc.toObject(TextMessage::class.java)}
                        1.0 -> {doc.toObject(ImageMessage::class.java)}
                        2.0 -> {doc.toObject(FileMessage::class.java)}
                        3.0 -> {doc.toObject(RecordMessage::class.java)}
                        else -> {}
                    }
                    messageList.add(message as FireMessage)
                }
                _messages.postValue(messageList)
            }
        }
    }


    //======Transition of Screen Methods
    val navigationToGroupDetail = MutableLiveData<Event<String>>()
    fun onGroupDetailClicked(){
        navigationToGroupDetail.value = Event("navigationToGroupDetail")
    }

    val navigationToSchedule = MutableLiveData<Event<String>>()
    fun onScheduleClicked(){
        navigationToSchedule.value = Event("navigationToSchedule")
    }

    val showBottomSheet = MutableLiveData<Event<String>>()
    fun showBottomSheet(){
        showBottomSheet.value = Event("show a bottom sheet")
    }

    val hideBottomSheet = MutableLiveData<Event<String>>()
    fun hideBottomSheet(){
        hideBottomSheet.value = Event("hide a bottom sheet")
    }

    val attachmentOfImage = MutableLiveData<Event<String>>()
    fun attachImage(){
        attachmentOfImage.value = Event("attach a Image")
    }

    val attachmentOfFile = MutableLiveData<Event<String>>()
    fun attachFile(){
        attachmentOfFile.value = Event("attach a file")
    }

    val recordingVoice = MutableLiveData<Event<String>>()
    fun recordVoice(){
        recordingVoice.value = Event("record Voice")
        //  StartActivityを呼び出し、ActivityResultでViewModelをVoiceURLを渡す処理にする。
    }

    //=====Message作成処理
    fun createTextMessage(){
        val message =
            TextMessage(
                FirebaseAuth.getInstance().uid,//TODO HostUserではなく、現在ログインしているユーザーを取得する。
                chatGroup.value?.getLogInUser(FirebaseAuth.getInstance().uid!!)?.userName,
                chatGroup.value?.getLogInUser(FirebaseAuth.getInstance().uid!!)?.userImage,
                0.0,
                editMessage.value,
                Date()) as FireMessage
        editMessage.postValue("")
        viewModelScope.launch {
            val result:Result<Int> = repository.createMessage(message,chatId)
            /*when(result){
              is Result.Success ->  //TODO アップロード成功時の処理を記述する。
              else //TODO アップロード失敗時、CoroutineScopeを終わらせてスコープの外でまとめて表示処理する。
            }*/
        }
    }

    fun createImageMessage(imageUri:Uri) {
        viewModelScope.launch {
                async{ repository.uploadPhoto(imageUri)}.await().also {
                    when(it){
                        is Result.Success -> {
                            val storageImageRef = it.data.path.plus(IMAGE_SIZE)
                            val message =
                                ImageMessage(
                                    FirebaseAuth.getInstance().uid,//TODO HostUserではなく、現在ログインしているユーザーを取得する。
                                    chatGroup.value?.getLogInUser(FirebaseAuth.getInstance().uid!!)?.userName,
                                    chatGroup.value?.getLogInUser(FirebaseAuth.getInstance().uid!!)?.userImage,
                                    1.0,
                                    storageImageRef,
                                    Date()
                                ) as FireMessage
                            val result: Result<Int> = repository.createMessage(message,chatId)
                            when(result){
                              is Result.Success ->  _notifier.value = !(notifier.value ?: false)
                              else -> Unit//TODO アップロード失敗時、CoroutineScopeを終わらせてスコープの外でまとめて表示処理する。
                            }
                        }
                        //else //TODO アップロード失敗時、CoroutineScopeを終わらせてスコープの外でまとめて表示処理する。
                        else -> {}
                    }
                }
            }
        }

    fun createFileMessage(fileUri:Uri) {
        viewModelScope.launch {
            //DONE UploadPhotoメソッドのままでアップロードできるのだろうか？いずれにしろ名称は書き換える。
            async{repository.uploadFile(fileUri)}.await().also {
                when(it){
                    is Result.Success -> {
                        val fileDownloadUrl = it.data.toString()//TODO　あまりにも通信量が大きくなるようであれば、コメントアウトする。
                        val message =
                            FileMessage(
                                FirebaseAuth.getInstance().uid,//TODO HostUserではなく、現在ログインしているユーザーを取得する。
                                chatGroup.value?.getLogInUser(FirebaseAuth.getInstance().uid!!)?.userName,
                                chatGroup.value?.getLogInUser(FirebaseAuth.getInstance().uid!!)?.userImage,
                                2.0,
                                fileUri.lastPathSegment.toString(),
                                fileDownloadUrl,
                                Date()) as FireMessage
                        val result:Result<Int> = repository.createMessage(message,chatId)
                        /*when(result){
                          is Result.Success ->  //TODO アップロード成功時の処理を記述する。
                          else //TODO アップロード失敗時、CoroutineScopeを終わらせてスコープの外でまとめて表示処理する。
                        }*/
                    }
                    //else //TODO アップロード失敗時、CoroutineScopeを終わらせてスコープの外でまとめて表示処理する。
                    else -> {}
                }
            }
        }
    }

    fun createRecordMessage(filePath:String,duration:String) {
        viewModelScope.launch {
            //DONE UploadPhotoメソッドのままでアップロードできるのだろうか？いずれにしろ名称は書き換える。
            async{repository.uploadRecord(filePath)}.await().also {
                when(it){
                    is Result.Success -> {
                        val recordDownloadUrl = it.data.toString()
                        val message =
                            RecordMessage(
                                FirebaseAuth.getInstance().uid,//TODO HostUserではなく、現在ログインしているユーザーを取得する。
                                chatGroup.value?.getLogInUser(FirebaseAuth.getInstance().uid!!)?.userName,
                                chatGroup.value?.getLogInUser(FirebaseAuth.getInstance().uid!!)?.userImage,
                                3.0,
                                recordDownloadUrl,
                                duration,
                                null,
                                null,
                                Date()) as FireMessage
                        val result:Result<Int> = repository.createMessage(message,chatId)
                        /*when(result){
                          is Result.Success ->  //TODO アップロード成功時の処理を記述する。
                          else //TODO アップロード失敗時、CoroutineScopeを終わらせてスコープの外でまとめて表示処理する。
                        }*/
                    }
                    //else //TODO アップロード失敗時、CoroutineScopeを終わらせてスコープの外でまとめて表示処理する。
                    else -> {}
                }
            }
        }
    }



    @AssistedFactory
    interface Factory{
        fun create(groupId: String): ChatRoomViewModel
    }

    companion object {
        private val TAG: String? = ChatRoomViewModel::class.simpleName
        private const val IMAGE_SIZE = "_400x400"
    }

}
