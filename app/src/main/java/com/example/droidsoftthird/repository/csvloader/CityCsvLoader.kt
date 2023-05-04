package com.example.droidsoftthird.repository.csvloader

import android.content.Context

class CityCsvLoader(private val context: Context) {

    companion object {
        private const val JAPAN_ALL_ADDRESS_CSV = "all_address_jp_20221009.csv"
        private const val JAPAN_CITY_CODE = 0
        private const val JAPAN_CITY_NAME = 1
        private const val JAPAN_CITY_NAME_KANA = 2
        private const val JAPAN_CITY_SPELL = 3
        private const val JAPAN_PREFECTURE_CODE = 4

        private const val CITY_ADDRESS_CSV = "city_address_jp_20221010.csv"
        private const val PREFECTURE_CODE = 0
        private const val CITY_CODE = 2
        private const val CITY_NAME = 3
        private const val CITY_LAT = 5
        private const val CITY_LNG = 6
    }

    private val cityLatLngData: Map<Int, Pair<Double, Double>> by lazy {
        context.assets.open(CITY_ADDRESS_CSV).bufferedReader().readLines().drop(1).map { line ->
            val data = line.split(",")
            data[CITY_CODE].toInt() to Pair(data[CITY_LAT].toDouble(), data[CITY_LNG].toDouble())
        }.toMap()
    }

    val cityLocalItems: List<CityLocalItem>

    init {
        cityLocalItems = context.assets.open(JAPAN_ALL_ADDRESS_CSV).bufferedReader().readLines().map { line ->
            val data = line.split(",")
            val cityCode = data[JAPAN_CITY_CODE].drop(1).dropLast(1).toInt()
            val latLng = cityLatLngData[cityCode] ?: Pair(0.0, 0.0)
            CityLocalItem(
                cityCode = cityCode,
                prefectureCode = data[JAPAN_PREFECTURE_CODE].drop(1).dropLast(1).toInt(),
                name = data[JAPAN_CITY_NAME].drop(1).dropLast(1),
                nameKana = data[JAPAN_CITY_NAME_KANA].drop(1).dropLast(1),
                spell = data[JAPAN_CITY_SPELL].drop(1).dropLast(1),
                latitude = latLng.first,
                longitude = latLng.second
            )
        }
    }

    data class CityLocalItem(
        val cityCode: Int,
        val prefectureCode: Int,
        val name: String,
        val nameKana: String?,
        val spell: String?,
        val latitude: Double,
        val longitude: Double
    )
}
