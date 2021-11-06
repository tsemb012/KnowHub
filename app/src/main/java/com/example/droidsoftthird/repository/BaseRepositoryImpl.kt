package com.example.droidsoftthird.repository

import android.net.Uri
import com.example.droidsoftthird.*
import com.example.droidsoftthird.model.Group
import com.example.droidsoftthird.model.RawSchedulePlan
import com.example.droidsoftthird.model.SchedulePlan
import com.example.droidsoftthird.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BaseRepositoryImpl<T> @Inject constructor(): BaseRepository {
    private val fireStore = FirebaseFirestore.getInstance()
    private val fireStorageRef = FirebaseStorage.getInstance().reference
    private val userId: String by lazy { FirebaseAuth.getInstance().currentUser.uid }

    override suspend fun getGroups(query: String): Result<List<Group>> = getListResult(query, Group::class.java)

    override suspend fun getGroup(groupId: String): Result<Group?> = //TODO GroupがNullである可能性のリスクをどこかで回収する。
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
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Result.Error(it))
                }
        }
    }


    override suspend fun uploadPhoto(uri: Uri): Result<StorageReference> {
        val photoRef = fireStorageRef.child("images/${UUID.randomUUID().toString()}")
        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                photoRef.putFile(uri)
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

    override suspend fun createGroup(group: Group): Result<Int> {
        val groupRef = fireStore.collection("groups").document()
        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                fireStore.runBatch { batch ->
                    batch.set(groupRef,group)
                    batch.update(groupRef,"members",FieldValue.arrayUnion(userId))
                }.addOnSuccessListener {
                    try {
                        continuation.resume(Result.Success(R.string.upload_success))
                    } catch (e: Exception) {
                        continuation.resume(Result.Error(e))
                    }
                }.addOnFailureListener { continuation.resume(Result.Error(it)) }
            }
        }
    }

    override suspend fun getUserProfile(): Result<UserProfile?> =
        withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                fireStore.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener {
                        try {
                            if (it != null){
                                continuation.resume(Result.Success(it.toObject()))
                            }else{
                                continuation.resume(Result.Success(null))
                            }
                        } catch (e: Exception) {
                            continuation.resume(Result.Error(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Error(it))
                    }
            }
        }

    override suspend fun createUserProfile(userProfile: UserProfile): Result<Int> {
        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                fireStore.collection("users")
                    .document(userId)
                    .set(userProfile)
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

    override suspend fun updateAuthProfile(authProfileUpdates:UserProfileChangeRequest): Result<Int> {
        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                Firebase.auth.currentUser
                    .updateProfile(authProfileUpdates)
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


    override suspend fun userJoinGroup(groupId: String): Result<Int> {

        val groupRef = fireStore.collection("groups").document(groupId)
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                fireStore.runBatch {batch ->

                    batch.update(groupRef,"members",FieldValue.arrayUnion(userId))
                    /*TODO
                    *   1. UserIdArrayをGroupのFieldに追加し、UIDを入れる。
                    *   2. CloudFunctionを用いて、userProfile内にGroupのフィールド情報を同期できるように設定する。
                    *   3. CloudFunctionを用いて、group内にUserProfileのuserImageのフィールド情報を同期できるように設定する。
                    *   */
                }.addOnSuccessListener {
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

    override suspend fun getSchedules(query: String): Result<List<RawSchedulePlan>> = getListResult(query, RawSchedulePlan::class.java)

    private suspend fun <T> getListResult(query: String, classType: Class<T>): Result<List<T>> =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getQuery(query)
                    .get()
                    .addOnSuccessListener {
                        try {
                            continuation.resume(Result.Success(it.toObjects(classType)))
                        } catch (e: Exception) {
                            continuation.resume(Result.Error(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Error(it))
                    }
            }
        }

    private fun getQuery(query: String): Query {
        return when(query){
            GroupQuery.ALL.value ->
                fireStore
                    .collection("groups")
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .limit(LIMIT)
            GroupQuery.MY_PAGE.value ->
                fireStore
                    .collection("groups")
                    .whereArrayContains("members",userId)
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .limit(LIMIT)
            ScheduleQuery.REGISTERED_ALL.value ->
                fireStore
                    .collection("schedules")
                    .whereArrayContains("registered_member",userId)
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .limit(LIMIT)
            else -> throw IllegalStateException()
        }
    }

    companion object {
        private const val  LIMIT = 50L
    }
}