package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.AddGroupViewModel
import com.example.droidsoftthird.R

class ActivityAreaDialogFragmentNext:DialogFragment() {

    private val viewModel: AddGroupViewModel by viewModels()
    private val prefectureList: Array<String> =
        resources.getStringArray(R.array.online_and_prefectures)
    private var selected = 0
    private val args = arguments
    private val previousSelected = args?.getInt("prefecture")
    private lateinit var items: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (previousSelected) {
            0 -> { }
            1 -> items = resources.getStringArray(R.array.Hokkaido)
            2 -> items = resources.getStringArray(R.array.Aomori)
            3 -> items = resources.getStringArray(R.array.Iwate)
            4 -> items = resources.getStringArray(R.array.Miyagi)
            5 -> items = resources.getStringArray(R.array.Akita)
            6 -> items = resources.getStringArray(R.array.Yamagata)
            7 -> items = resources.getStringArray(R.array.Fukushima)
            8 -> items = resources.getStringArray(R.array.Ibaraki)
            9 -> items = resources.getStringArray(R.array.Tochigi)
            10 -> items = resources.getStringArray(R.array.Gunma)
            11 -> items = resources.getStringArray(R.array.Saitama)
            12 -> items = resources.getStringArray(R.array.Chiba)
            13 -> items = resources.getStringArray(R.array.Tokyo)
            14 -> items = resources.getStringArray(R.array.Kanagawa)
            15 -> items = resources.getStringArray(R.array.Niigata)
            16 -> items = resources.getStringArray(R.array.Toyama)
            17 -> items = resources.getStringArray(R.array.Ishikawa)
            18 -> items = resources.getStringArray(R.array.Fukui)
            19 -> items = resources.getStringArray(R.array.Yamanashi)
            20 -> items = resources.getStringArray(R.array.Nagano)
            21 -> items = resources.getStringArray(R.array.Gifu)
            22 -> items = resources.getStringArray(R.array.Shizuoka)
            23 -> items = resources.getStringArray(R.array.Aichi)
            24 -> items = resources.getStringArray(R.array.Mie)
            25 -> items = resources.getStringArray(R.array.Shiga)
            26 -> items = resources.getStringArray(R.array.Kyoto)
            27 -> items = resources.getStringArray(R.array.Osaka)
            28 -> items = resources.getStringArray(R.array.Hyogo)
            29 -> items = resources.getStringArray(R.array.Nara)
            30 -> items = resources.getStringArray(R.array.Wakayama)
            31 -> items = resources.getStringArray(R.array.Tottori)
            32 -> items = resources.getStringArray(R.array.Shimane)
            33 -> items = resources.getStringArray(R.array.Okayama)
            34 -> items = resources.getStringArray(R.array.Hiroshima)
            35 -> items = resources.getStringArray(R.array.Yamaguchi)
            36 -> items = resources.getStringArray(R.array.Tokuhsima)
            37 -> items = resources.getStringArray(R.array.Kagawa)
            38 -> items = resources.getStringArray(R.array.Ehime)
            39 -> items = resources.getStringArray(R.array.Kochi)
            40 -> items = resources.getStringArray(R.array.Fukuoka)
            41 -> items = resources.getStringArray(R.array.Saga)
            42 -> items = resources.getStringArray(R.array.Nagasaki)
            43 -> items = resources.getStringArray(R.array.Kumamoto)
            44 -> items = resources.getStringArray(R.array.Ooita)
            45 -> items = resources.getStringArray(R.array.Miyazaki)
            46 -> items = resources.getStringArray(R.array.Kagoshima)
            47 -> items = resources.getStringArray(R.array.Okinawa)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
            .setTitle(R.string.activity_area)
            .setIcon(R.drawable.ic_baseline_location_on_24)
            .setSingleChoiceItems(items, selected) { dialog, which -> selected = which }
            .setPositiveButton(R.string.done) { dialog, which ->{
                viewModel.postPrefecture(prefectureList[previousSelected!!])
                viewModel.postCity(items[which]) }
            }
            .setNeutralButton(R.string.cancel) { dialog, which -> Unit}
            .create()
        }?: throw IllegalStateException("Activity cannot be null")
    }

}
