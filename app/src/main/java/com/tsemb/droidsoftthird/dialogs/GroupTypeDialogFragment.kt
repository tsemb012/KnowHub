package com.tsemb.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.tsemb.droidsoftthird.GroupAddViewModel
import com.tsemb.droidsoftthird.R
import com.tsemb.droidsoftthird.model.domain_model.GroupType

class GroupTypeDialogFragment: DialogFragment() {

    private val viewModel: GroupAddViewModel by viewModels({requireParentFragment()})
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val items = GroupType.values().map { getString(it.displayNameId) }.toTypedArray()
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.group_type)
                .setIcon(R.drawable.baseline_local_fire_department_24)
                .setItems(items) { _, which -> viewModel.postGroupType(GroupType.values()[which]) }
            return    builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}