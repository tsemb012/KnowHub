package com.example.droidsoftthird.repository

import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.droidsoftthird.*
import com.example.droidsoftthird.api.MainApi
import com.example.droidsoftthird.model.domain_model.*
import com.example.droidsoftthird.model.domain_model.fire_model.Group
import com.example.droidsoftthird.model.domain_model.fire_model.RawScheduleEvent
import com.example.droidsoftthird.model.domain_model.fire_model.UserProfile
import com.example.droidsoftthird.model.infra_model.json.request.PutUserToGroup
import com.example.droidsoftthird.repository.DataStoreRepository.Companion.TOKEN_ID_KEY
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
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

/*    override suspend fun postNewUser(signup: SignUpJson): User? =
        mainApi.postNewUser(PostSignUp.Request(signup)).body()?.toEntity()
        //TODO Resultを付けて返した方が良いかを検討する。→ Jsonを戻す時の構造体を再検討する。*/

    override suspend fun getGroups(query: String): Result<List<Group>> = getListResult(query, Group::class.java)

    override suspend fun certifyAndRegister(tokenID: String) {
        mapOf("Authorization" to "Bearer $tokenID").let {
            mainApi.postTokenId(it, userId)
        }
    }

    override suspend fun signUpAndFetchToken(email: String, password: String) : Result<String> =
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

    override suspend fun singIn(email: String, password: String): Result<String> =
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
    withContext(Dispatchers.IO){suspendCoroutine { continuation ->
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

    override suspend fun createGroup(group: ApiGroup): String? =
        mainApi.createGroup(group.toJson()).body()?.message

    override suspend fun fetchGroupDetail(groupId: String): ApiGroupDetail {//TODO エラーハンドリングをまとめる
        val response = mainApi.fetchGroup(groupId)
        if (response.isSuccessful) {
            return response.body()?.toEntity() ?: throw Exception("response body is null")
        } else {
            throw Exception("fetchGroupDetail is failed")
        }
    }

    override suspend fun fetchGroups(page: Int) : List<ApiGroup> = //TODO ドメイン層を作り、ビジネスロジックを詰め込む必要がある。
        mainApi.fetchGroups(page = page).body()?.map { it.toEntity() } ?: listOf()

    override suspend fun fetchJoinedGroups() : List<ApiGroup> = //TODO ユーザーIDを渡す位置を再検討する
        mainApi.fetchGroups(userId = userId).body()?.map { it.toEntity() } ?: listOf()

    override suspend fun userJoinGroup(groupId: String): String? {
        val response = mainApi.putUserToGroup(groupId, PutUserToGroup(userId))
        return if (response.isSuccessful) {
            return response.body()?.message
        } else {
            throw Exception("userJoinGroup is failed")
        }
    }

    override suspend fun fetchUser(): UserDetail = mainApi.fetchUser(userId).toEntity()
    override suspend fun updateUserDetail(userDetail: UserDetail) = mainApi.putUserDetail(userId, userDetail.copy(userId = userId).toJson()).message
    override suspend fun createUser(userDetail: UserDetail): String = mainApi.putUserDetail(userId, userDetail.copy(userId = userId).toJson()).message

    override suspend fun searchIndividualPlace(query: String, viewPort: ViewPort): List<Place> =
        mainApi.getIndividualPlace(
                query = query,
                language = LANGUAGE_JP,
                northLat = viewPort.northEast?.latitude ?: 0.0,
                eastLng = viewPort.northEast?.longitude ?: 0.0,
                southLat = viewPort.southWest?.latitude ?: 0.0,
                westLng = viewPort.southWest?.longitude ?: 0.0
        ).body()?.map { it.toEntity() } ?: listOf()

    override suspend fun searchByText(query: String, centerPoint: LatLng, type: String, radius: Int): List<Place> =
        mainApi.getPlacesByText(
                query = query,
                type = type,
                language = LANGUAGE_JP,
                region = REGION_JP,
                centerLat = centerPoint.latitude,
                centerLng = centerPoint.longitude,
                radius = radius.toString(),
        ).body()?.map { it.toEntity() } ?: listOf()

    override suspend fun searchByPoi(centerPoint: LatLng, type: String, radius: Int): List<Place> =
        mainApi.getPlacesByPoi(
                type = type,
                language = LANGUAGE_JP,
                centerLat = centerPoint.latitude,
                centerLng = centerPoint.longitude,
                radius = radius.toString(),
        ).body()?.map { it.toEntity() } ?: listOf()

    override suspend fun fetchPlaceDetail(placeId: String): PlaceDetail? =
        mainApi.getPlaceDetail(
                placeId = placeId,
                language = LANGUAGE_JP
        ).body()?.first()?.toEntity()

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
        private const val LIMIT = 50L
        private const val LANGUAGE_JP = "ja"//言語設定する際に、直接Preferenceから取得するようにする。
        private const val REGION_JP = "jp"//設定する際に、直接Preferenceから取得するようにする。
        const val GROUP_ALL = "group_all"
        const val GROUP_MY_PAGE = "group_my_page"
        const val SCHEDULE_REGISTERED_ALL = "schedule_registered_all"
    }
}
