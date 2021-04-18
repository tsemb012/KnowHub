package com.example.droidsoftthird

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels

class FacilityEnvironmentDialogFragment : DialogFragment() {

    private val viewModel: AddGroupViewModel by viewModels()
    private var selected = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val items = arrayOf(
            getString(R.string.library),
            getString(R.string.cafe_restaurant),
            getString(R.string.rental_space),
            getString(R.string.study_place),
            getString(R.string.park),
            getString(R.string.online),
            getString(R.string.other)
        )

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.facility_environment)
                .setIcon(R.drawable.ic_baseline_location_city_24)
                .setSingleChoiceItems(items, selected,
                    DialogInterface.OnClickListener { dialog, which -> selected = which })
                .setPositiveButton(getString(R.string.done),
                    DialogInterface.OnClickListener { dialog, which ->
                         viewModel.postFacilityEnvironment(items[selected])
                    })
                .setNeutralButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, which -> Unit })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

