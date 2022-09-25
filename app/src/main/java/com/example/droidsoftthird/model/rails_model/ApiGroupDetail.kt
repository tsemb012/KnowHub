package com.example.droidsoftthird.model.rails_model

data class ApiGroupDetail (
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
        val members: List<ApiMember>
)

class ApiMember (
        val userId:String,
)//TODO 必要に応じて増設する
