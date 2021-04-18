package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.AddGroupViewModel
import com.example.droidsoftthird.R
import com.google.android.material.slider.RangeSlider


//TODO Better to Modify to ViewBinding from FindViewBy
class AgeRangeDialogFragment: DialogFragment() {

    private val viewModel: AddGroupViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_age_range, null, false)
        val rs:RangeSlider = view.findViewById(R.id.rangeSlider)
        rs.stepSize = 1f
        rs.thumbTintList = AppCompatResources.getColorStateList(requireContext(),R.color.range_slider)
        var minAge = Math.round(rs.getValues()[0])
        var maxAge = Math.round(rs.getValues()[1])
        rs.addOnChangeListener(RangeSlider.OnChangeListener { _, _, _ ->
            var tv = view.findViewById<TextView>(R.id.tv_range_slider)
            tv.text = String.format("%s~%s歳", Math.round(rs.values[0]), Math.round(rs.values[1]))
            minAge = Math.round(rs.values[0])
            maxAge = Math.round(rs.values[1])
        })

        return activity?.let {
            val builder = AlertDialog.Builder(activity)
            builder
                .setTitle(R.string.age_range)
                .setIcon(R.drawable.ic_baseline_elevator_24)
                .setPositiveButton(R.string.done){ _, _ ->
                    viewModel.postMinAge(minAge)
                    viewModel.postMaxAge(maxAge)
                }
                .setNeutralButton(R.string.cancel,
                    DialogInterface.OnClickListener { _, _ -> Unit})
                .setView(view)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
