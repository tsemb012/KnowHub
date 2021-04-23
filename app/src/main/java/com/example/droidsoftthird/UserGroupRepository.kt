package com.example.droidsoftthird

import android.net.Uri
import com.example.droidsoftthird.model.Group
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserGroupRepository {
    private val fireStore = FirebaseFirestore.getInstance()
    private val fireStorageRef = FirebaseStorage.getInstance().reference


    suspend fun getAllGroups():Result<List<Group>> =
        withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                fireStore.collection("groups")
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .limit(LIMIT)
                    .get()
                    .addOnSuccessListener {
                        try {
                            continuation.resume(Result.Success(it.toObjects(Group::class.java)))
                            Timber.tag("check_result2").d(it.toObjects(Group::class.java).toString())
                        } catch (e: Exception) {
                            continuation.resume(Result.Error(e))
                            Timber.tag("check_result3").d(e.toString())
                        }

                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Error(it))
                        Timber.tag("check_result4").d(it.toString())
                    }
            }

        }

    suspend fun getGroup(groupId: String):Result<Group?> = //TODO GroupがNullである可能性のリスクをどこかで回収する。
    withContext(Dispatchers.IO){
        suspendCoroutine { continuation ->
            fireStore.collection("groups")
                .document(groupId)
                .get()
                .addOnSuccessListener {
                    try {
                        continuation.resume(Result.Success(it.toObject()))
                    } catch (e: Exception) {
                        continuation.resume(Result.Error(e))
                        Timber.tag("check_result3").d(e.toString())
                    }

                }
                .addOnFailureListener {
                    continuation.resume(Result.Error(it))
                    Timber.tag("check_result4").d(it.toString())
                }
        }

    }


    suspend fun uploadPhoto(uri: Uri):Result<StorageReference> {
        val photoRef = fireStorageRef.child("images/${UUID.randomUUID().toString()}")
        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                    photoRef
                        .putFile(uri)
                        .addOnSuccessListener {
                            try {
                                continuation.resume(Result.Success(it.storage))
                            } catch (e: Exception) {
                                continuation.resume(Result.Error(e))
                            }
                        }
                        .addOnFailureListener {
                            continuation.resume(Result.Error(it))
                        }
                }
            }
        }

    suspend fun uploadGroup(group: Group):Result<Int> {
        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                fireStore.collection("groups")
                    .document()
                    .set(group)
                    .addOnSuccessListener {
                        try {
                            continuation.resume(Result.Success(R.string.upload_success))
                        } catch (e: Exception) {
                            continuation.resume(Result.Error(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Error(it))
                    }
            }
        }
    }




    companion object {
        private const val  LIMIT = 50L
    }
}
