package com.tsemb.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.tsemb.droidsoftthird.GroupAddViewModel
import com.example.droidsoftthird.R
import com.google.android.material.slider.Slider
import kotlin.math.roundToInt


class NumberPersonsDialogFragment:DialogFragment() {
    private val viewModel: GroupAddViewModel by viewModels({requireParentFragment()})

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view: View = layoutInflater.inflate(R.layout.dialog_number_persons, null, false)
        val slider: Slider = view.findViewById(R.id.slider_person)

        slider.stepSize = 1f
        slider.thumbTintList = AppCompatResources.getColorStateList(requireContext(), R.color.range_slider)
        slider.value = 10f // 初期位置を10に設定

        var maxNumberPerson = slider.value.roundToInt()
        slider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            val tv = view.findViewById<TextView>(R.id.tv_slider)
            tv.text = String.format("%s人", value.roundToInt())
            maxNumberPerson = value.roundToInt()
        })
        //TODO SliderのOnChangeListenerとBindingAdapterが競合しないか確認する。

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.number_persons)
                .setIcon(R.drawable.ic_baseline_groups_24)
                .setPositiveButton(R.string.done,
                    DialogInterface.OnClickListener { _, _ ->
                        viewModel.postMaxNumberPerson(maxNumberPerson)
                    })
                .setNeutralButton(R.string.cancel,
                    DialogInterface.OnClickListener { _, _ -> })
                .setView(view)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
