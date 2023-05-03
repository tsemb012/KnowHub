package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.GroupAddViewModel
import com.example.droidsoftthird.R
import com.example.droidsoftthird.model.domain_model.GroupType

class GroupTypeDialogFragment: DialogFragment() {

    private val viewModel: GroupAddViewModel by viewModels({requireParentFragment()})
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val items = GroupType.toArrayForDisplay().map { getString(it.displayNameId) }.toTypedArray()
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.group_name)
                .setIcon(R.drawable.ic_baseline_group_24)
                .setItems(items) { _, which -> viewModel.postGroupType(GroupType.values()[which]) }
            return    builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}