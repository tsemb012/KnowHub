package com.example.droidsoftthird.repository

import android.net.Uri
import com.example.droidsoftthird.QueryType
import com.example.droidsoftthird.R
import com.example.droidsoftthird.Result
import com.example.droidsoftthird.model.Group
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
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserGroupRepository @Inject constructor() {
    private val fireStore = FirebaseFirestore.getInstance()
    private val fireStorageRef = FirebaseStorage.getInstance().reference


    suspend fun getGroups(query: String): Result<List<Group>> =
        withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                    getQuery(query)
                    .get()
                    .addOnSuccessListener {
                        try {
                            continuation.resume(Result.Success(it.toObjects(Group::class.java)))
                            Timber.tag("check_result1-2").d(it.toObjects(Group::class.java).toString())
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

    private fun getQuery(query: String): Query {
        return when(query){
            QueryType.ALL.value ->
                fireStore
                    .collection("groups")
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .limit(LIMIT)
            QueryType.MY_PAGE.value ->
                fireStore
                    .collection("groups")
                    .whereArrayContains("members",FirebaseAuth.getInstance().currentUser.uid )
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .limit(LIMIT)
            else ->
                fireStore
                    .collection("groups")
        }
    }

    suspend fun getGroup(groupId: String): Result<Group?> = //TODO GroupがNullである可能性のリスクをどこかで回収する。
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


    suspend fun uploadPhoto(uri: Uri): Result<StorageReference> {
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




    suspend fun createGroup(group: Group): Result<Int> {
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

    suspend fun getUserProfile(): Result<UserProfile?> =
        withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                fireStore.collection("users")
                    .document(FirebaseAuth.getInstance().currentUser.uid)
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
                            Timber.tag("check_result3").d(e.toString())
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Result.Error(it))
                        Timber.tag("check_result4").d(it.toString())
                    }
            }
        }

    suspend fun createUserProfile(userProfile: UserProfile): Result<Int> {
        return withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                fireStore.collection("users")
                    .document(FirebaseAuth.getInstance().currentUser.uid)
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

    suspend fun updateAuthProfile(authProfileUpdates:UserProfileChangeRequest): Result<Int> {
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


    suspend fun userJoinGroup(groupId: String): Result<Int> {

        val groupRef = fireStore.collection("groups").document(groupId)
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                fireStore.runBatch {batch ->

                    batch.update(groupRef,"members",FieldValue.arrayUnion(FirebaseAuth.getInstance().currentUser.uid))
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


    companion object {
        private const val  LIMIT = 50L
    }
}

//TODO Batch処理を行う際の参考資料。
/*suspend fun createFavoriteGroup(groupId: String): Result<Int> {

    val subUserRef = fireStore.collection("groups").document(groupId).collection("users").document(firebaseUid)
    val subGroupRef = fireStore.collection("users").document(firebaseUid).collection("groups").document(groupId)

    return withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            fireStore.runBatch {batch ->
                batch.set(subUserRef,hashMapOf("userId" to firebaseUid))
                batch.set(subGroupRef,hashMapOf("groupId" to groupId))
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
}*/
