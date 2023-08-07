package com.tsemb.droidsoftthird

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.tsemb.droidsoftthird.GroupAddViewModel
import com.tsemb.droidsoftthird.model.domain_model.FacilityEnvironment

class FacilityEnvironmentDialogFragment : DialogFragment() {

    private val viewModel: GroupAddViewModel by viewModels({requireParentFragment()})
    private var selected = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val items = FacilityEnvironment.values().map { getString(it.displayNameId) }.toTypedArray()
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.facility_environment)
                .setIcon(R.drawable.ic_baseline_location_city_24)
                .setSingleChoiceItems(items, selected) { _, which -> selected = which }
                .setPositiveButton(getString(R.string.done)) { _, _ -> viewModel.postFacilityEnvironment(
                    FacilityEnvironment.values()[selected]) }
                .setNeutralButton(R.string.cancel) { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

