package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.AddGroupViewModel
import com.example.droidsoftthird.R
import com.example.droidsoftthird.model.Place
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class ActivityAreaDialogFragmentNext:DialogFragment() {

    //TODO 宣言部分を整理する。
    //
    private val viewModel: AddGroupViewModel by viewModels({ requireParentFragment() })
    private var selected = 0
    private var previousSelected: Int? = 0
    private lateinit var places: MutableList<Place>
    private lateinit var items: MutableList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        previousSelected = arguments?.getInt("prefecture")
        when (previousSelected) {
            0 -> { }

            1 -> CSVRead("hokkaido_1.csv")
            2 -> CSVRead("aomori_2.csv")
            3 -> CSVRead("iwate_3.csv")
            4 -> CSVRead("miyagi_4.csv")
            5 -> CSVRead("akita_5.csv")
            6 -> CSVRead("yamagata_6.csv")
            7 -> CSVRead("fukushima_7.csv")
            8 -> CSVRead("ibaraki_8.csv")
            9 -> CSVRead("tochigi_9.csv")
            10 -> CSVRead("gunma_10.csv")
            11 -> CSVRead("hokkaido_1.csv")
            12 -> CSVRead("chiba_12.csv")
            13 -> CSVRead("tokyo_13.csv")
            14 -> CSVRead("kanagawa_14.csv")
            15 -> CSVRead("niigata_15.csv")
            16 -> CSVRead("toyama_16.csv")
            17 -> CSVRead("ishikawa_17.csv")
            18 -> CSVRead("fukui_18.csv")
            19 -> CSVRead("yamanashi_19.csv")
            20 -> CSVRead("nagano_20.csv")
            21 -> CSVRead("gifu_21.csv")
            22 -> CSVRead("shizuoka_22.csv")
            23 -> CSVRead("aichi_23.csv")
            24 -> CSVRead("mie_24.csv")
            25 -> CSVRead("shiga_25.csv")
            26 -> CSVRead("kyoto_26.csv")
            27 -> CSVRead("osaka_27.csv")
            28 -> CSVRead("hyogo_28.csv")
            29 -> CSVRead("nara_29.csv")
            30 -> CSVRead("wakayama_30.csv")
            31 -> CSVRead("tottori_31.csv")
            32 -> CSVRead("shimane_32.csv")
            33 -> CSVRead("okayama_33.csv")
            34 -> CSVRead("hiroshima_34.csv")
            35 -> CSVRead("yamaguchi_35.csv")
            36 -> CSVRead("tokushima_36.csv")
            37 -> CSVRead("kagawa_37.csv")
            38 -> CSVRead("ehime_38.csv")
            39 -> CSVRead("kochi_39.csv")
            40 -> CSVRead("fukuoka_40.csv")
            41 -> CSVRead("saga_41.csv")
            42 -> CSVRead("nagasai_42.csv")
            43 -> CSVRead("kumamoto_43.csv")
            44 -> CSVRead("oita_44.csv")
            45 -> CSVRead("miyazaki_45.csv")
            46 -> CSVRead("kagoshima_46.csv")
            47 -> CSVRead("okinawa_47.csv")

        }
    }

    private fun CSVRead(csvFile: String){
        try {
            val assetManager = requireContext().resources.assets
            val inputStream = assetManager.open(csvFile)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferReader = BufferedReader(inputStreamReader)
            var line: String
            while (bufferReader.readLine().also { line = it } != null) {

                //カンマ区切りで１つづつ配列に入れる
                val data = Place()
                val rowData = line.split(",").toTypedArray()

                items.add(rowData[1])

                //CSVの左([0]番目)から順番にセット
                data.prefecture = rowData[0]
                data.city = rowData[1]
                data.prefectureAndCity = rowData[2]
                data.latitude = rowData[3].toDouble()
                data.longitude = rowData[4].toDouble()
                places.add(data)

            }
            bufferReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val prefectureList: Array<String> = resources.getStringArray(R.array.online_and_prefectures)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
            .setTitle(R.string.activity_area)
            .setIcon(R.drawable.ic_baseline_location_on_24)
            .setSingleChoiceItems(items.toTypedArray(), selected) { dialog, which -> selected = which }
            .setPositiveButton(R.string.done, DialogInterface.OnClickListener { dialog, which ->
                viewModel.postPlace(places[selected]) })
            .setNeutralButton(R.string.cancel) { dialog, which -> Unit}
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }


}
