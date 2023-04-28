package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.infra_model.json.request.PostUserDetailJson
import com.squareup.moshi.JsonAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class UserDetail (
        val userId: String,
        val userName: String,
        val userImage: String,
        val comment:String,
        val gender: String,
        val birthday: LocalDate,
        val area: Area,
        val groups: List<ApiGroup>,
        val events: List<ItemEvent>,
) {
    enum class Gender { MALE, FEMALE, NO_ANSWER }
    fun toJson() =
        PostUserDetailJson(
                userId = userId,
                userName = userName,
                userImage = userImage,
                comment = comment,
                gender = gender,
                birthday = birthday.format(DateTimeFormatter.ISO_LOCAL_DATE),
                prefectureCode = area.prefecture?.prefectureCode ?: -1,
                cityCode = area.city?.cityCode ?: -1,
        )
}

internal val initializedUserDetail get() =
        UserDetail(
                userId = "",
                userName = "",
                userImage = "",
                comment = "",
                gender = "",
                birthday = LocalDate.now(),
                area = Area(null, null),
                groups = listOf(),
                events = listOf(),
        )


