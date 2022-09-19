package com.example.droidsoftthird.model.json

import com.example.droidsoftthird.model.User

class UserJson (
    val id: String,
    val name: String,
    val email: String,
    //TODO APIに合わせてJsonを追加する。
)

fun UserJson.toEntity() = User(id, name, email)
