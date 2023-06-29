package com.example.droidsoftthird.model.domain_model

data class ChatGroup(
    val groupId: String?,
    val groupName: String,
    val hostUserId: String,
    val members: List<SimpleUser> = listOf(),
) {
    val hostUser: SimpleUser get() = members.first { it.userId == hostUserId }
}
