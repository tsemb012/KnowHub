package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.droidsoftthird.R
import com.example.droidsoftthird.model.domain_model.Area
import com.example.droidsoftthird.model.domain_model.City
import com.example.droidsoftthird.model.domain_model.Prefecture

class AreaDialogFragment(
        private val onExceptionListener: (String) -> Unit,
        private val onConfirmListener: (Area) -> Unit,
):DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        buildPrefectureDialog(prefectures = resources.getStringArray(R.array.online_and_prefectures))

    private fun buildPrefectureDialog(prefectures: Array<String>):AlertDialog {
        var selectedPrefectureCode = 0
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.activity_area)
            .setSingleChoiceItems(prefectures,
                0) { _, selectedPrefecture -> selectedPrefectureCode = selectedPrefecture }
            .setIcon(R.drawable.ic_baseline_location_on_24)
            .setPositiveButton(getString(R.string.Next)) { _, _ ->
                if (selectedPrefectureCode != 0) {
                    LocalAreaDialogFragment(selectedPrefectureCode, onConfirmListener).show(
                        parentFragmentManager,
                        "activity_area_next")
                } else {
                    onExceptionListener(prefectures[0])
                }
            }
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .create()
    }
}


class LocalAreaDialogFragment(private val prefectureCode: Int, private val onConfirmListener: (Area) -> Unit) :DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val cityMap = getCityMap(prefectureCode)
        val cityArray = cityMap.values.toMutableList().also { it.add(0, "指定しない") }.toTypedArray()

        return buildCityDialog(prefectureCode, cityArray, cityMap)
    }

    private fun getCityMap(prefectureCode: Int): Map<Int, String> =
        requireContext().assets.open(JAPAN_ALL_ADDRESS_CSV).bufferedReader().readLines()
            .filter { it.split(",")[JAPAN_PREFECTURE_CODE].drop(1).dropLast(1).toInt() == prefectureCode }
            .associateBy(
                { it.split(",")[JAPAN_CITY_CODE].drop(1).dropLast(1).toInt() },
                { it.split(",")[JAPAN_CITY_NAME].drop(1).dropLast(1) }
            )

    private fun buildCityDialog(prefectureCode: Int, cityArray: Array<String>, cityMap: Map<Int, String>, ): AlertDialog {
        var selectedCityCode = -1
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.activity_area)
            .setIcon(R.drawable.ic_baseline_location_on_24)
            .setSingleChoiceItems(cityArray, 0) { _, which -> selectedCityCode = which }
            .setPositiveButton(R.string.done) { _, _ ->
                val cityCode = cityMap.keys.elementAt(selectedCityCode)
                val area = buildArea(prefectureCode, cityCode)
                onConfirmListener(area)
            }
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .create()
    }

    private fun buildArea(prefectureCode: Int, cityCode: Int): Area {

        val prefecture =
            requireContext().assets.open(PREFECTURE_ADDRESS_CSV).bufferedReader().readLines().first { it.split(",")[PREFECTURE_CODE].toInt() == prefectureCode }.split(",")
                .let {
                    Prefecture(
                        prefectureCode = it[PREFECTURE_CODE].toInt(),
                        name = it[PREFECTURE_NAME],
                        spell = it[PREFECTURE_SPELL],
                        capital = Prefecture.Capital(
                            name = it[PREFECTURE_CAPITAL_NAME],
                            spell = it[PREFECTURE_CAPITAL_SPELL],
                            latitude = it[PREFECTURE_CAPITAL_LAT].toDouble(),
                            longitude = it[PREFECTURE_CAPITAL_LNG].toDouble()
                        )
                    )
                }

        if (cityCode == 0) { return Area(prefecture = prefecture, city = null) }

        val spell = requireContext().assets.open(JAPAN_ALL_ADDRESS_CSV)
            .bufferedReader()
            .readLines()
            .first { it.split(",")[JAPAN_CITY_CODE].drop(1).dropLast(1).toInt() == cityCode }
            .split(",")[JAPAN_CITY_SPELL]
            .capitalize()

        val rawCity = requireContext().assets.open(CITY_ADDRESS_CSV)
            .bufferedReader()
            .readLines()
            .first{ it.split(",")[CITY_CODE].toIntOrNull() == cityCode }
            .split(",")

        val city = City(
            cityCode = cityCode,
            name = rawCity[CITY_NAME],
            spell = spell,
            latitude = rawCity[CITY_LAT].toDouble(),
            longitude = rawCity[CITY_LNG].toDouble(),
        )
        return Area(prefecture, city)
    }

    companion object { //TODO 全てのCSVを統一する　→　GithubActionで更新　→　Roomなどで永続管理
        private const val JAPAN_ALL_ADDRESS_CSV = "all_address_jp_20221009.csv"
        private const val JAPAN_CITY_CODE = 0
        private const val JAPAN_CITY_NAME = 1
        private const val JAPAN_CITY_SPELL = 3
        private const val JAPAN_PREFECTURE_CODE = 4


        private const val PREFECTURE_ADDRESS_CSV = "prefecture_address_jp.csv"
        private const val PREFECTURE_CODE = 0
        private const val PREFECTURE_NAME = 1
        private const val PREFECTURE_SPELL = 3
        private const val PREFECTURE_CAPITAL_NAME = 4
        private const val PREFECTURE_CAPITAL_SPELL = 5
        private const val PREFECTURE_CAPITAL_LAT = 6
        private const val PREFECTURE_CAPITAL_LNG = 7

        private const val CITY_ADDRESS_CSV = "city_address_jp_20221010.csv"
        private const val CITY_CODE = 2
        private const val CITY_NAME = 3
        private const val CITY_LAT = 5
        private const val CITY_LNG = 6
    }

}

