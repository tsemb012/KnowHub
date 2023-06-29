package com.example.droidsoftthird.model.infra_model.json.response

import com.example.droidsoftthird.model.domain_model.ChatGroup
import com.squareup.moshi.Json

data class GetChatGroupJson(
    @Json(name = "group_id")
    val groupId: String,
    @Json(name = "group_name")
    val groupName: String,
    @Json(name = "host_id")
    val hostUserId: String,
    val members: List<GetSimpleUserJson> = listOf(),
) {
    fun toEntity() =
        ChatGroup(
            groupId = groupId,
            groupName = groupName,
            hostUserId = hostUserId,
            members = members.map { it.toEntity() },
        )
}
