package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import com.example.droidsoftthird.R
import com.example.droidsoftthird.databinding.DialogSeekBarBinding
import kotlin.math.roundToInt

class SeekBarFragment(
        private val title: String,
        private val initNumber:Int,
        private val unit: String,
        private val onPositiveListener: (Int) -> Unit
): DialogFragment() {

    private val binding = DialogSeekBarBinding.inflate(layoutInflater)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding.seekBar.apply {
            stepSize = 1f
            thumbTintList = AppCompatResources.getColorStateList(requireContext(), R.color.range_slider)
            binding.seekBarText.text = String.format(initNumber.toString() + unit)
            addOnChangeListener { _, value, _ -> binding.seekBarText.text = String.format(value.roundToInt().toString() + unit) }
        }

        return AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setView(binding.root)
                .setPositiveButton(R.string.positive_ok) { _, _ -> onPositiveListener(binding.seekBar.value.toInt()) }
                .setNegativeButton(R.string.negative_cancel) { dialog, _ -> dialog.dismiss() }
                .create()
    }
}