package com.example.droidsoftthird.repository

import android.net.Uri
import com.example.droidsoftthird.R
import com.example.droidsoftthird.Result
import com.example.droidsoftthird.model.domain_model.fire_model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/*他と干渉しないChatRoomのレポジトリーのみ独立させている。
* しかしChatに多くの機能を追加することになった場合、通常のレポジトリーと統合することも検討する。
* */

class MessageRepository  @Inject constructor(){
    private val fireStore = FirebaseFirestore.getInstance()
    private val fireStorageRef = FirebaseStorage.getInstance().reference
    private val firebaseUid = FirebaseAuth.getInstance().currentUser.uid

    suspend fun uploadPhoto(uri: Uri): Result<StorageReference> {

        val photoRef = fireStorageRef.child("chat_images/${firebaseUid}/${UUID.randomUUID().toString()}")
        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                photoRef
                    .putFile(uri)
                    .addOnSuccessListener {
                        try {
                            continuation.resume(Result.Success(it.storage))
                        } catch (e: Exception) {
                            continuation.resume(Result.Failure(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Failure(it))
                    }
            }
        }
    }

    suspend fun uploadFile(uri: Uri): Result<Uri> {

        val fileRef = fireStorageRef.child("chat_files/${firebaseUid}/${UUID.randomUUID().toString()}")
        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                val uploadTask = fileRef.putFile(uri)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            continuation.resume(Result.Failure(it))
                        }
                    }
                    fileRef.downloadUrl
                }.addOnSuccessListener {
                        try {
                            continuation.resume(Result.Success(it))
                        } catch (e: Exception) {
                            continuation.resume(Result.Failure(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Failure(it))
                    }
            }
        }
    }

    suspend fun uploadRecord(filePath: String) : Result<Uri> {

        val recordRef = fireStorageRef.child("chat_records/${firebaseUid}/${Date().time.toString()}")
        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                val uploadTask = recordRef.putFile(Uri.fromFile(File(filePath)))
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            continuation.resume(Result.Failure(it))
                        }
                    }
                    recordRef.downloadUrl
                }.addOnSuccessListener {
                    try {
                        continuation.resume(Result.Success(it))
                    } catch (e: Exception) {
                        continuation.resume(Result.Failure(e))
                    }
                }
                    .addOnFailureListener {
                        continuation.resume(Result.Failure(it))
                    }
            }
        }
    }

    suspend fun createMessage(message: Message, groupId:String): Result<Int> {

        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                fireStore
                    .collection("groups").document(groupId)
                    .collection("messages").document()
                    .set(message)
                    .addOnSuccessListener {
                        try {
                            continuation.resume(Result.Success(R.string.upload_success))
                        } catch (e: Exception) {
                            continuation.resume(Result.Failure(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Failure(it))
                    }
            }
        }
    }

    fun getChatEvents(groupId: String): Flow<QuerySnapshot> = callbackFlow {
        var messagesCollection:CollectionReference? = null
        try{
            messagesCollection = fireStore
                .collection("groups").document(groupId)
                .collection("messages")

        }catch(e:Throwable){
            close(e)
        }

        val subscription =
            messagesCollection?.orderBy("timestamp",Query.Direction.ASCENDING)?.addSnapshotListener{ snapshot, _->
            if (snapshot == null){return@addSnapshotListener}
            try {
                trySend(snapshot).isSuccess
            }catch(e:Throwable){

            }
        }

        awaitClose { subscription?.remove() }
    }
}