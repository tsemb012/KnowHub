package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.AddGroupViewModel
import com.example.droidsoftthird.R
import com.example.droidsoftthird.databinding.DialogNumberPersonsBinding
import com.google.android.material.slider.RangeSlider


class NumberPersonsDialogFragment:DialogFragment() {
    private val viewModel: AddGroupViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogNumberPersonsBinding.inflate(LayoutInflater.from(context));
        val rs = binding.rangeSlider
        rs.stepSize = 1f
        rs.thumbTintList = AppCompatResources.getColorStateList(requireContext(), R.color.range_slider
        )

        var minNumberPerson = Math.round(rs.getValues()[0])
        var maxNumberPerson = Math.round(rs.getValues()[1])
        rs.addOnChangeListener(RangeSlider.OnChangeListener { _, _, _ ->
            binding.tvRangeSlider.text = String.format("%s~%s人", minNumberPerson, maxNumberPerson)
            minNumberPerson = Math.round(rs.getValues()[0])
            maxNumberPerson = Math.round(rs.getValues()[1])
        })
        //TODO SliderのOnChangeListenerとBindingAdapterが競合しないか確認する。

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.number_persons)
                .setIcon(R.drawable.ic_baseline_groups_24)
                .setPositiveButton(R.string.done,
                    DialogInterface.OnClickListener { _, _ ->
                        viewModel.postMinNumberPerson(minNumberPerson)
                        viewModel.postMaxNumberPerson(maxNumberPerson)
                    })
                .setNeutralButton(R.string.cancel,
                    DialogInterface.OnClickListener { _, _ -> })
                .setView(view)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

}
