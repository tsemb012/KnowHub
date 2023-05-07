package com.example.droidsoftthird.model.infra_model.json.request

import com.example.droidsoftthird.model.domain_model.SimpleGroup

data class GetSimpleGroupJson(
    val groupId: String?,
    val groupName: String,
) {
    fun toEntity() = SimpleGroup(
            groupId = groupId,
            groupName = groupName,
    )
}
