package com.example.droidsoftthird.repository

import android.net.Uri
import com.example.droidsoftthird.*
import com.example.droidsoftthird.api.MainApi
import com.example.droidsoftthird.model.User
import com.example.droidsoftthird.model.fire_model.Group
import com.example.droidsoftthird.model.fire_model.RawScheduleEvent
import com.example.droidsoftthird.model.fire_model.UserProfile
import com.example.droidsoftthird.model.json.SignUpJson
import com.example.droidsoftthird.model.json.toEntity
import com.example.droidsoftthird.model.request.PostSignUp
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

class BaseRepositoryImpl @Inject constructor(
        private val mainApi: MainApi
): RailsApiRepository, FireStoreRepository {
    private val fireStore = FirebaseFirestore.getInstance()//TODO 全てHiltに入れてインジェクトから取得する。
    private val fireStorageRef = FirebaseStorage.getInstance().reference
    private val userId: String by lazy { FirebaseAuth.getInstance().currentUser.uid
    }

    override suspend fun postNewUser(signup: SignUpJson): User? =
        mainApi.postNewUser(PostSignUp.Request(signup)).body()?.toEntity()
        //TODO Resultを付けて返した方が良いかを検討する。→ Jsonを戻す時の構造体を再検討する。

    override suspend fun getGroups(query: String): Result<List<Group>> = getListResult(query, Group::class.java)

    suspend fun singIn(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                it.result?.user?.let { user ->
                                    continuation.resume(Result.Success(User(user.uid, user.email!!, user.displayName)))
                                }
                            } else {
                                continuation.resume(Result.Failure(it.exception ?: IllegalStateException()))
                            }
                        }
            }
        }

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
                        continuation.resume(Result.Failure(e))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Result.Failure(it))
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
                            continuation.resume(Result.Failure(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Failure(it))
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
                        continuation.resume(Result.Failure(e))
                    }
                }.addOnFailureListener { continuation.resume(Result.Failure(it)) }
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
                            continuation.resume(Result.Failure(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Failure(it))
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
                            continuation.resume(Result.Failure(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Failure(it))
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
                            continuation.resume(Result.Failure(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Failure(it))
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
                        continuation.resume(Result.Failure(e))
                    }
                }
                    .addOnFailureListener {
                        continuation.resume(Result.Failure(it))
                    }
            }
        }
    }

    override suspend fun getSchedules(query: String): Result<List<RawScheduleEvent>> = getListResult(query, RawScheduleEvent::class.java)

    private suspend fun <T> getListResult(query: String, classType: Class<T>): Result<List<T>> =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getQuery(query)
                    .get()
                    .addOnSuccessListener {
                        try {
                            continuation.resume(Result.Success(it.toObjects(classType)))
                        } catch (e: Exception) {
                            continuation.resume(Result.Failure(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Failure(it))
                    }
            }
        }

    private fun getQuery(query: String): Query {
        return when(query){
            GROUP_ALL->
                fireStore
                    .collection("groups")
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .limit(LIMIT)
            GROUP_MY_PAGE ->
                fireStore
                    .collection("groups")
                    .whereArrayContains("members",userId)
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .limit(LIMIT)
            SCHEDULE_REGISTERED_ALL ->
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
        const val GROUP_ALL = "group_all"
        const val GROUP_MY_PAGE = "group_my_page"
        const val SCHEDULE_REGISTERED_ALL = "schedule_registered_all"
    }
}
