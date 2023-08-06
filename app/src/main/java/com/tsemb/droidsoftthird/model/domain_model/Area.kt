package com.tsemb.droidsoftthird.model.domain_model

import com.tsemb.droidsoftthird.model.infra_model.json.request.AreaJson

data class Area (//居住地情報にプロパティを追加していく可能性があるのでArea内に県と市の情報を内包させる
    val prefecture: Prefecture?,
    val city: City?,
) {
        fun toJson() = AreaJson(
                prefecture = prefecture?.toJson(),
                city = city?.toJson()
        )
}

enum class AreaCategory { PREFECTURE, CITY }

