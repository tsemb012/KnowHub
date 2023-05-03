package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.R
import com.example.droidsoftthird.model.infra_model.json.request.PostGroupJson

data class EditedGroup(
    val groupId: String?,
    val hostUserId: String,
    val storageRef: String,
    val groupName: String,
    val groupIntroduction: String,
    val groupType: GroupType,
    val prefecture_code: Int,
    val city_code: Int,
    val facilityEnvironment: FacilityEnvironment,
    val basis: String,
    val frequency:Int,
    val minAge:Int,
    val maxAge:Int,
    val maxNumberPerson:Int,
    val isChecked:Boolean,
) {
        fun toJson() =
                PostGroupJson(
                        hostId = hostUserId,
                        name = groupName,
                        introduction = groupIntroduction,
                        groupType = groupType.name.lowercase(),
                        prefecture_code = prefecture_code,
                        city_code = city_code,
                        facilityEnvironment = facilityEnvironment.name.lowercase(),
                        frequencyBasis = basis,
                        frequencyTimes = frequency,
                        maxAge = maxAge,
                        minAge = minAge,
                        maxNumber = maxNumberPerson,
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
    val maxNumberPerson :Int,
    val isChecked:Boolean,
)

enum class GroupType(val displayNameId : Int) {
    NONE(R.string.no_set),
    SEMINAR(R.string.seminar),
    WORKSHOP(R.string.workshop),
    MOKUMOKU(R.string.mokumoku),
    OTHER(R.string.other)
}

enum class FacilityEnvironment(val displayNameId : Int) {
    NONE(R.string.no_set),
    LIBRARY(R.string.library),
    CAFE_RESTAURANT(R.string.cafe_restaurant),
    RENTAL_SPACE(R.string.rental_space),
    CO_WORKING_SPACE(R.string.co_working_space),
    PAID_STUDY_SPACE(R.string.paid_study_space),
    PARK(R.string.park),
    ONLINE(R.string.online),
    OTHER(R.string.other)
}

/*enum class Basis(val displayNameId : Int) {
    NONE(R.string.no_set),
    DAILY(R.string.daily),
    WEEKLY(R.string.weekly),
    MONTHLY(R.string.monthly),
    OTHER(R.string.other)
}*/
