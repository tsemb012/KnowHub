package com.tsemb.droidsoftthird.model.domain_model

import com.tsemb.droidsoftthird.model.infra_model.json.request.PrefectureJson

data class Prefecture(
        val prefectureCode: Int,
        val name: String,
        val spell: String,
        val capital: Capital
) {
    data class Capital (
            val name: String,
            val spell: String,
            val latitude: Double,
            val longitude: Double
    )

    fun toJson() =
        PrefectureJson(
                prefectureCode = prefectureCode,
                name = name,
                spell = spell,
                capitalName = capital.name,
                capitalSpell = capital.spell,
                capitalLatitude = capital.latitude,
                capitalLongitude = capital.longitude
        )
}
