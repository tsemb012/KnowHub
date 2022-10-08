package com.example.droidsoftthird.model.rails_model

import com.example.droidsoftthird.model.request.PostGroup

data class ApiGroup (//TODO ApiGroupからGroupに名前を変更する//Firebaseのモデルが全て置き換わったら
    val groupId: String?,
    val hostUserId: String,
    val storageRef: String,
    val groupName: String,
    val groupIntroduction: String,
    val groupType: String,
    val prefecture: String,
    val city: String,
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
        PostGroup(
                hostId = hostUserId,
                name = groupName,
                introduction = groupIntroduction,
                groupType = groupType,
                prefecture = prefecture,
                city = city,
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