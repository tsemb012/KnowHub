package com.example.droidsoftthird.model.fire_model

import com.example.droidsoftthird.model.request.PostGroup

data class Group (
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
        PostGroup.Request(
                hostId = hostUserId,
                name = groupName,
                introduction = groupIntroduction,
                type = groupType,
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
