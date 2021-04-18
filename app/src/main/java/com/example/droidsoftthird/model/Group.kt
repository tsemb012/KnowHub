package com.example.droidsoftthird.model

import android.text.TextUtils
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


data class Group (
    var hostUserId: String?,
    var storageRef: String,
    var groupName: String,
    var groupIntroduction: String,
    var groupType: String,
    var prefecture: String,
    var city: String,
    var facilityEnvironment: String,
    var basis: String,
    var frequency:String,
    var minAge:Int,
    var maxAge:Int,
    var minNumberPerson:Int,
    var maxNumberPerson :Int,
    var isChecked:Boolean,
    @ServerTimestamp
    val timeStamp: Date? = null //TODO TimeStampで不具合が発生しないか検証する。
)
