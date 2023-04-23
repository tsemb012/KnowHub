package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.infra_model.json.request.AreaJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import java.time.ZonedDateTime

data class GetUserDetailJson (
    @Json(name = "id")
    val userId: String,
    @Json(name = "user_name")
    val userName: String,
    @Json(name = "user_image")
    val userImage: String,
    val comment:String,
    val gender: String,
    val age: Int,
    val area: AreaJson,
    val groups: List<GetGroupJson>,
    val events: List<GetItemEventJson>
) {
    fun toEntity(
        localDateTimeAdapter: JsonAdapter<ZonedDateTime>,
    ) =
        UserDetail(
                userId = userId,
                userName = userName,
                userImage = userImage,
                comment = comment,
                gender = gender,
                age = age,
                area = area.toEntity(),
                groups = groups.map { it.toEntity() },
                events = events.map { it.toEntity(localDateTimeAdapter) }
        )
}
