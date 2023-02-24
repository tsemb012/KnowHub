package com.example.droidsoftthird.model.infra_model.json

import com.example.droidsoftthird.model.domain_model.User

class UserJson (
    val id: String,
    val name: String,
    val email: String,
    //TODO APIに合わせてJsonを追加する。
)

fun UserJson.toEntity() = User(id, name, email)
