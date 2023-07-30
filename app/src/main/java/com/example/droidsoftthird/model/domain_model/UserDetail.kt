package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.infra_model.json.request.PostUserDetailJson
import java.time.LocalDate
import java.time.Period
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
    val events: List<EventItem>,
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

    fun getJapanese(gender: String): String {
        return when(gender) {
            "male" -> "男性"
            "female" -> "女性"
            else -> "未回答"
        }
    }

    private val age: Int get() = Period.between(birthday, LocalDate.now()).years
    val formattedBirthday: String get() = birthday.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 (${age}歳)"))

    val residentialArea: String
        get() {
            val prefecture = area.prefecture?.name
            val city = area.city?.name

            return when {
                prefecture != null && city != null -> "$prefecture, $city"
                prefecture != null -> prefecture
                else -> "非公開"
            }
        }


}

internal val initializedUserDetail get() =
    UserDetail(
        userId = "",
        userName = "",
        userImage = "",
        comment = "",
        gender = "no_answer",
        birthday = LocalDate.now(),
        area = Area(null, null),
        groups = listOf(),
        events = listOf(),
    )


