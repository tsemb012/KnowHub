package com.example.droidsoftthird.utils

import com.google.firebase.auth.FirebaseAuth

object AuthUtil {//外部に切り出すのは、テストをしやすくするという点で正解だったりする。

    val firebaseAuthInstance: FirebaseAuth by lazy {
        println("firebaseAuthInstance.:")
        FirebaseAuth.getInstance()
    }

    fun getAuthId(): String {
        return firebaseAuthInstance.currentUser?.uid.toString()
    }

}