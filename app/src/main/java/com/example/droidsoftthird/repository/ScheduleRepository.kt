package com.example.droidsoftthird.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class ScheduleRepository @Inject constructor(){
    private val fireStore = FirebaseFirestore.getInstance()
    private val fireStorageRef = FirebaseStorage.getInstance().reference
    private val firebaseUid = FirebaseAuth.getInstance().currentUser.uid



}
