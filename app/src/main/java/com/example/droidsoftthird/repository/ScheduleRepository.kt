package com.example.droidsoftthird.repository

import com.example.droidsoftthird.ScheduleState
import com.example.droidsoftthird.model.Schedule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ScheduleRepository @Inject constructor(){

    private val fireStore = FirebaseFirestore.getInstance()
    private val fireStorageRef = FirebaseStorage.getInstance().reference
    private val firebaseUid = FirebaseAuth.getInstance().currentUser.uid


    suspend fun getSchedules(state: ScheduleState, groupId: String?): com.example.droidsoftthird.Result<List<Schedule>> =
        withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                getQuery(state,groupId)
                    .get()
                    .addOnSuccessListener {
                        try {
                            continuation.resume(com.example.droidsoftthird.Result.Success(it.toObjects(Schedule::class.java)))
                        } catch (e: Exception) {
                            continuation.resume(com.example.droidsoftthird.Result.Error(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(com.example.droidsoftthird.Result.Error(it))
                    }
            }
        }

    private fun getQuery(state: ScheduleState, groupId: String?): Query {

        return when(groupId){
            "ALL"->
                fireStore
                    .collection("schedules")
                    .whereEqualTo("members.${firebaseUid}",state)
                    .orderBy("timeStamp", Query.Direction.DESCENDING)
                    .limit(LIMIT)
            else ->
                fireStore
                    .collection("schedules")
                    .whereEqualTo("members.${firebaseUid}",state)
                    .whereEqualTo("groupId",groupId)
                    .orderBy("timeStamp", Query.Direction.DESCENDING)
                    .limit(LIMIT)
        }
    }

    suspend fun getGroupArray(): com.example.droidsoftthird.Result<List<String>> =
        withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                fireStore
                    .collection("users")
                    .document(firebaseUid)
                    .get()
                    .addOnSuccessListener {
                        try {
                            continuation.resume(com.example.droidsoftthird.Result.Success(it.get("groups")as(List<String>)))
                        } catch (e: Exception) {
                            continuation.resume(com.example.droidsoftthird.Result.Error(e))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(com.example.droidsoftthird.Result.Error(it))
                    }
            }
        }

    companion object {
        private const val  LIMIT = 100L
    }
}
