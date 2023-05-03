package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.GroupAddViewModel
import com.example.droidsoftthird.R
import com.example.droidsoftthird.model.domain_model.FrequencyBasis

class LeaningFrequencyDialogFragmentNext:DialogFragment() {

    private val viewModel: GroupAddViewModel by viewModels({requireParentFragment()})
    private var previousSelected:Int? = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = requireActivity().layoutInflater.inflate(R.layout.dialog_learning_frequency, null, false)
        val np = view.findViewById<View>(R.id.numberPicker) as NumberPicker
        val tv = view.findViewById<TextView>(R.id.basis)
        previousSelected = arguments?.getInt("learning_frequency")
        when (previousSelected) {
            1 -> {
                tv.setText(FrequencyBasis.WEEKLY.displayNameId)
                np.minValue = 1
                np.maxValue = 7
            }
            2 -> {
                tv.setText(FrequencyBasis.MONTHLY.displayNameId)
                np.minValue = 1
                np.maxValue = 31
            }
        }

        return activity?.let {
            val builder = AlertDialog.Builder(activity)
            builder
                .setTitle(R.string.learning_frequency)
                .setIcon(R.drawable.ic_baseline_date_range_24)
                .setPositiveButton(R.string.done) { _, _ ->
                    viewModel.postBasis(FrequencyBasis.values()[previousSelected ?: 3])
                    viewModel.postFrequency(np.value)
                }
                .setNeutralButton(R.string.cancel) { _, _ -> Unit }
                .setView(view)
                .create() //変数の数と変数名だけで匿名クラスを省略し、ラムダ式に変換できるよということ？
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}



