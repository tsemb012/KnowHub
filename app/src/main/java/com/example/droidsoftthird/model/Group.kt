package com.example.droidsoftthird.model

import android.text.TextUtils
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


data class Group (//FireStoreで使用する場合は、Nullableかつ初期値にNullを入れる必要がある。
    val hostUserId: String? = null,
    val storageRef: String? = null,
    var groupName: String? = null,
    var groupIntroduction: String? = null,
    var groupType: String? = null,
    var prefecture: String? = null,
    var city: String? = null,
    var facilityEnvironment: String? = null,
    var basis: String? = null,
    var frequency:String? = null,
    var minAge:Int? = null,
    var maxAge:Int? = null,
    var minNumberPerson:Int? = null,
    var maxNumberPerson :Int? = null,
    @field:JvmField
    var isChecked:Boolean? = null,
    @DocumentId
    val documentId: String? = null,
    @ServerTimestamp
    val timeStamp: Date? = null
)
