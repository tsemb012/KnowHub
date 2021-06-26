package com.example.droidsoftthird.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class UserProfile (
    val userImageRef: String? = null,
    val backgroundImageRef:String? = null,
    val userName: String? = null,
    val userIntroduction: String? = null,
    val gender: Int? = null,
    val age: Int? = null,
    val prefecture: String? = null,
    val city: String? = null,
    @DocumentId
    val userId: String? = null,
    @ServerTimestamp
    val timeStamp: Date? = null
)