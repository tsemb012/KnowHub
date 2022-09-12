package com.example.droidsoftthird.repository

import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.droidsoftthird.*
import com.example.droidsoftthird.api.MainApi
import com.example.droidsoftthird.model.User
import com.example.droidsoftthird.model.fire_model.Group
import com.example.droidsoftthird.model.fire_model.RawScheduleEvent
import com.example.droidsoftthird.model.fire_model.UserProfile
import com.example.droidsoftthird.model.json.SignUpJson
import com.example.droidsoftthird.model.json.toEntity
import com.example.droidsoftthird.model.request.PostSignUp
import com.example.droidsoftthird.repository.DataStoreRepository.Companion.TOKEN_ID_KEY
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
        private val mainApi: MainApi,
        private val dataStore: DataStore<Preferences>
): RailsApiRepository, FirebaseRepository, DataStoreRepository {
    private val fireStore = FirebaseFirestore.getInstance()//TODO 全てHiltに入れてインジェクトから取得する。
    private val fireStorageRef = FirebaseStorage.getInstance().reference
    private val userId: String by lazy { FirebaseAuth.getInstance().currentUser.uid }

    override suspend fun saveTokenId(tokenId: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(TOKEN_ID_KEY)] = tokenId
        }
    }

    override suspend fun clearTokenId() {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(TOKEN_ID_KEY))
        }
    }

    override suspend fun postNewUser(signup: SignUpJson): User? =
        mainApi.postNewUser(PostSignUp.Request(signup)).body()?.toEntity()
        //TODO Resultを付けて返した方が良いかを検討する。→ Jsonを戻す時の構造体を再検討する。

    override suspend fun getGroups(query: String): Result<List<Group>> = getListResult(query, Group::class.java)

    suspend fun certifyTokenId(tokenID: String) {
        mapOf("Authorization" to "Bearer $tokenID").let {
            mainApi.postTokenId(it)
        }
    }

    override suspend fun signUp(email: String, password: String) : Result<String> =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { signInTask ->
                                    if (signInTask.isSuccessful && signInTask.result != null) {
                                        FirebaseAuth.getInstance().currentUser.getIdToken(true)
                                            .addOnCompleteListener { idTokenTask ->
                                                if (idTokenTask.isSuccessful && idTokenTask.result?.token != null) {
                                                    continuation.resume(Result.Success(idTokenTask.result?.token!!))
                                                } else {
                                                    continuation.resume(Result.Failure(Exception("idTokenTask is failed")))
                                                }
                                            }
                                    } else {
                                        continuation.resume(Result.Failure(signInTask.exception ?:IllegalStateException()))
                                    }
                                }
                        } else {
                            continuation.resume(Result.Failure(task.exception ?: IllegalStateException()))
                        }
                    }
            }
        }//TODO　ネスト深すぎ。。要リファクタリング

    suspend fun singIn(email: String, password: String): Result<String> =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { signInTask ->
                        if (signInTask.isSuccessful && signInTask.result != null) {
                            FirebaseAuth.getInstance().currentUser.getIdToken(true)
                                .addOnCompleteListener { idTokenTask ->
                                    if (idTokenTask.isSuccessful && idTokenTask.result?.token != null) {
                                        continuation.resume(Result.Success(idTokenTask.result?.token!!))
                                    } else {
                                        continuation.resume(Result.Failure(Exception("idTokenTask is failed")))
                                    }
                                }
                        } else {
                            continuation.resume(Result.Failure(signInTask.exception ?:IllegalStateException()))
                        }
                    }
            }
        }//TODO 要リファクタリング

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

    override suspend fun createGroup(group: Group): String? =
        mainApi.createGroup(group.toJson()).body()?.message

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
