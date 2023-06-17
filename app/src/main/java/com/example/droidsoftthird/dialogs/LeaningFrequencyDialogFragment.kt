package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.GroupAddViewModel
import com.example.droidsoftthird.R
import com.example.droidsoftthird.model.domain_model.FrequencyBasis

class LeaningFrequencyDialogFragment:DialogFragment() {

    private var selected = 0
    private val viewModel: GroupAddViewModel by viewModels({requireParentFragment()})

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let{
            val builder = AlertDialog.Builder(it)
            val items = FrequencyBasis.values().map { basis -> getString(basis.displayNameId) }.toTypedArray()
            builder
                .setTitle(R.string.event_frequency)
                .setIcon(R.drawable.ic_baseline_date_range_24)
                .setSingleChoiceItems(items, selected) { _, which -> selected = which }
                .setPositiveButton(getString(R.string.Next)) { _, _ ->
                    if (selected != 0 || selected != 1) {
                        val dialogNext = LeaningFrequencyDialogFragmentNext()
                        val args = Bundle()
                        args.putInt("learning_frequency", selected)
                        dialogNext.arguments = args
                        dialogNext.show(parentFragmentManager, "activity_area_next")
                    } else {
                        viewModel.postBasis(FrequencyBasis.values()[selected])
                        viewModel.postFrequency(-1)
                    }
                }
                .setNeutralButton(R.string.cancel) { _, _ -> Unit }
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }
}


