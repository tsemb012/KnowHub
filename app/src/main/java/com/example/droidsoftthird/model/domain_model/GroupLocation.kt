package com.example.droidsoftthird.model.domain_model

data class GroupLocation (
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val groupCount : Int
    //TODO 市区町村の情報を追加する
)