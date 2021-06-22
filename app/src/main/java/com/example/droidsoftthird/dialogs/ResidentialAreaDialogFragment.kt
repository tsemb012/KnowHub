package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.ui.createProfile.CreateProfileViewModel
import com.example.droidsoftthird.R

class ResidentialAreaDialogFragment : DialogFragment() {


    private var selected = 0
    private val viewModel: CreateProfileViewModel by viewModels({requireParentFragment()})

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val prefectureList: Array<String> = resources.getStringArray(R.array.online_and_prefectures)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.residential_area)
                .setIcon(R.drawable.ic_baseline_location_on_24)
                .setSingleChoiceItems(prefectureList, selected,
                    DialogInterface.OnClickListener() { _, which ->
                        selected = which
                    })
                .setPositiveButton(getString(R.string.Next),
                    DialogInterface.OnClickListener() { _, which ->
                        if (selected != 0) {
                            val dialog_next = ResidentialAreaDialogFragmentNext()
                            val args = Bundle()
                            args.putInt("prefecture", selected)
                            dialog_next.arguments = args
                            dialog_next.show(
                                parentFragmentManager,
                                "residential_area_next"
                            );
                        } else {
                            viewModel.postPrefecture(prefectureList[0])
                        }
                    }).setNeutralButton(
                    R.string.cancel,
                    DialogInterface.OnClickListener() { dialog, which -> Unit }
                )
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
