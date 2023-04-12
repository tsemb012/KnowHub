package com.example.droidsoftthird.model.domain_model.fire_model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class FireUserProfile (
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

    //TODO DateAPI　から　Date &TimeAPIに変換する。
    //TODO RawProfileからProfileに変換する使用に変更する。GroupのPOJO見直しする際に。
)