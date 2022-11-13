package com.example.droidsoftthird.model.domain_model

import com.example.droidsoftthird.model.request.AreaJson

data class Area (//居住地情報にプロパティを追加していく可能性があるのでArea内に県と市の情報を内包させる
        val prefecture: Prefecture,
        val city: City?,
) {
        fun toJson() = AreaJson(
                prefecture = prefecture.toJson(),
                city = city?.toJson()
        )
}
