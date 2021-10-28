package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.GroupAddViewModel
import com.example.droidsoftthird.R
import timber.log.Timber

class GroupTypeDialogFragment: DialogFragment() {

    private val viewModel: GroupAddViewModel by viewModels({requireParentFragment()})
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val items = arrayOf(
            getString(R.string.seminar), getString(R.string.workshop),
            getString(R.string.mokumoku), getString(R.string.other)
        )
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.group_name)
                .setIcon(R.drawable.ic_baseline_group_24)
                .setItems(items) { _, which ->
                    Timber.tag("check_item[which]").d(items[which])
                    Timber.tag("viewModel2").d(viewModel.toString())
                    viewModel.postGroupType(items[which])

                }//TODO 継承関係はクリアしたと思われるが、ViewModelが生存していない様子。→　別のやり方(八木の方法)でViewModelを生成してみる。
            return    builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}