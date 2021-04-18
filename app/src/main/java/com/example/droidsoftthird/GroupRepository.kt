package com.example.droidsoftthird

import android.net.Uri
import com.example.droidsoftthird.model.Group
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GroupRepository {
    private val fireStore = FirebaseFirestore.getInstance()
    private val fireStorageRef = FirebaseStorage.getInstance().reference


    suspend fun getAllGroups():Result<List<Group>> =
        withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                fireStore.collection("groups")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(LIMIT)
                    .get()
                    .addOnSuccessListener {
                        try {
                            continuation.resume(Result.Success(it.toObjects()))
                        } catch (e: Exception) {
                            continuation.resume(Result.Error(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Error(it))
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


    companion object {
        private const val  LIMIT = 50L
    }
}
