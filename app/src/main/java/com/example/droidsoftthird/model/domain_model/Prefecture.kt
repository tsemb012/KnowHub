package com.example.droidsoftthird.model.domain_model

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
}
