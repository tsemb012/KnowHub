package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.AddGroupViewModel
import com.example.droidsoftthird.R

class GroupTypeDialogFragment: DialogFragment() {


    private val viewModel: AddGroupViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val items = arrayOf(
            getString(R.string.seminar), getString(R.string.workshop),
            getString(R.string.mokumoku), getString(R.string.other)
        )
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.group_name)
                .setIcon(R.drawable.ic_baseline_group_24)
                .setItems(items,
                    DialogInterface.OnClickListener { _, which ->
                        viewModel.postGroupType(items[which])
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}