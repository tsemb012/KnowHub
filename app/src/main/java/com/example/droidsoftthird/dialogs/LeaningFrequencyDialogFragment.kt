package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.AddGroupViewModel
import com.example.droidsoftthird.R

class LeaningFrequencyDialogFragment:DialogFragment() {

    private var selected = 0
    private val viewModel: AddGroupViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let{
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.learning_frequency)
                .setIcon(R.drawable.ic_baseline_date_range_24)
                .setSingleChoiceItems(
                    resources.getStringArray(R.array.frequently_basis),
                    selected,
                    DialogInterface.OnClickListener { dialog, which -> selected = which })
                .setPositiveButton(getString(R.string.Next),
                    DialogInterface.OnClickListener { dialog, which ->
                        if (selected != 0) {
                            val dialogNext = LeaningFrequencyDialogFragmentNext()
                            val args = Bundle()
                            args.putInt("learning_frequency", selected)
                            dialogNext.arguments = args
                            dialogNext.show(requireActivity().supportFragmentManager, "activity_area_next")
                        } else {
                            viewModel.postFrequency(R.string.everyday.toString())
                        }
                    })
                .setNeutralButton(R.string.cancel, DialogInterface.OnClickListener { dialog, which -> Unit}
                )
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }
}


