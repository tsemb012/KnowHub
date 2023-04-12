package com.example.droidsoftthird.model.domain_model

data class GroupCountByArea (
    val category : String,
    val code: String,
    val prefectureName: String,
    val cityName: String?,
    val latitude: Double,
    val longitude: Double,
    val groupCount : Int
)