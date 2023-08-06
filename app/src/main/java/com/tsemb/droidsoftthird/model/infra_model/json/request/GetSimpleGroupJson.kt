package com.tsemb.droidsoftthird.model.infra_model.json.request

import com.tsemb.droidsoftthird.model.domain_model.SimpleGroup
import com.squareup.moshi.Json

data class GetSimpleGroupJson(
    @Json (name = "id")
    val groupId: String?,
    @Json (name = "name")
    val groupName: String,
) {
    fun toEntity() = SimpleGroup(
            groupId = groupId,
            groupName = groupName,
    )
}
