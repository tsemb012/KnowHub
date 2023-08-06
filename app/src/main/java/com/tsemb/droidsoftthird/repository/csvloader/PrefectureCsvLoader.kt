package com.tsemb.droidsoftthird.repository.csvloader

import android.content.Context

class PrefectureCsvLoader(private val context: Context) {

    companion object {
        private const val PREFECTURE_ADDRESS_CSV = "prefecture_address_jp.csv"
        private const val PREFECTURE_CODE = 0
        private const val PREFECTURE_NAME = 1
        private const val PREFECTURE_NAME_KANA = 2
        private const val PREFECTURE_SPELL = 3
        private const val PREFECTURE_CAPITAL_NAME = 4
        private const val PREFECTURE_CAPITAL_SPELL = 5
        private const val PREFECTURE_CAPITAL_LAT = 6
        private const val PREFECTURE_CAPITAL_LNG = 7
    }

    val prefectureLocalItems: List<PrefectureLocalItem>

    init {
        prefectureLocalItems = context.assets.open(PREFECTURE_ADDRESS_CSV).bufferedReader().readLines().map { line ->
            val data = line.split(",")
            PrefectureLocalItem(
                code = data[PREFECTURE_CODE].toInt(),
                name = data[PREFECTURE_NAME],
                nameKana = data[PREFECTURE_NAME_KANA],
                spell = data[PREFECTURE_SPELL],
                capitalName = data[PREFECTURE_CAPITAL_NAME],
                capitalSpell = data[PREFECTURE_CAPITAL_SPELL],
                capitalLat = data[PREFECTURE_CAPITAL_LAT].toDouble(),
                capitalLng = data[PREFECTURE_CAPITAL_LNG].toDouble()
            )
        }
    }

    data class PrefectureLocalItem(
        val code: Int,
        val name: String,
        val nameKana: String,
        val spell: String,
        val capitalName: String,
        val capitalSpell: String,
        val capitalLat: Double,
        val capitalLng: Double
    )
}
