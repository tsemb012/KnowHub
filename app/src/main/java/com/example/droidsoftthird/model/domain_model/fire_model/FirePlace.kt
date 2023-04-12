package com.example.droidsoftthird.model.domain_model.fire_model

data class FirePlace(
    val isOnline: Boolean,
    val prefecture: String,
    val city: String,
    val latitude:Double,
    val longitude:Double,
)
//TODO frequencyを追加
