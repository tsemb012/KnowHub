package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.infra_model.json.request.PostGroupJson

data class EditedGroup (
        val groupId: String?,
        val hostUserId: String,
        val storageRef: String,
        val groupName: String,
        val groupIntroduction: String,
        val groupType: String,
        val prefecture_code: Int,
        val city_code: Int,
        val facilityEnvironment: String,
        val basis: String,
        val frequency:Int,
        val minAge:Int,
        val maxAge:Int,
        val minNumberPerson:Int,
        val maxNumberPerson :Int,
        val isChecked:Boolean,
) {
        fun toJson() =
                PostGroupJson(
                        hostId = hostUserId,
                        name = groupName,
                        introduction = groupIntroduction,
                        groupType = groupType,
                        prefecture_code = prefecture_code,
                        city_code = city_code,
                        facilityEnvironment = facilityEnvironment,
                        frequencyBasis = basis,
                        frequencyTimes = frequency,
                        maxAge = maxAge,
                        minAge = minAge,
                        maxNumber = maxNumberPerson,
                        minNumber = maxNumberPerson,
                        isSameSexuality = isChecked,
                        imageUrl = storageRef,
                )
}

data class ApiGroup (//TODO ApiGroupからGroupに名前を変更する//Firebaseのモデルが全て置き換わったら
    val groupId: String?,
    val hostUserId: String,
    val storageRef: String,
    val groupName: String,
    val groupIntroduction: String,
    val groupType: String,
    val prefecture: String,//TODO Areaに変更する。
    val city: String,
    val facilityEnvironment: String,
    val basis: String,
    val frequency:Int,
    val minAge:Int,
    val maxAge:Int,
    val minNumberPerson:Int,
    val maxNumberPerson :Int,
    val isChecked:Boolean,
)