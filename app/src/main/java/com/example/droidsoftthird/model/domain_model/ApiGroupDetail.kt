package com.example.droidsoftthird.model.domain_model

data class ApiGroupDetail(//TODO ApiGroupからGroupに名前を変更する//Firebaseのモデルが全て置き換わったら
        val groupId: String?,
        val hostUserId: String,
        val storageRef: String,
        val groupName: String,
        val groupIntroduction: String,
        val groupType: GroupType,
        val prefecture: String?,
        val city: String?,
        val isOnline: Boolean,
        val facilityEnvironment: FacilityEnvironment,
        val style: Style,
        val basis: FrequencyBasis,
        val frequency:Int,
        val minAge:Int,
        val maxAge:Int,
        val maxNumberPerson:Int,
        val isChecked:Boolean,
        val members: List<ApiMember>
)

class ApiMember (
        val userId:String,
)//TODO 必要に応じて増設する
