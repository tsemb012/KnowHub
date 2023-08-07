package com.tsemb.droidsoftthird.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate
import java.util.Calendar
import android.widget.NumberPicker
import com.tsemb.droidsoftthird.R

class BirthdayDialogFragment(private val onPositiveClickListener: (LocalDate) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_birthday, null)

        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker).apply {
            minValue = Calendar.getInstance().get(Calendar.YEAR) - 120
            maxValue = Calendar.getInstance().get(Calendar.YEAR)
            value = Calendar.getInstance().get(Calendar.YEAR) - 20 // Default selected year
        }

        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker).apply {
            minValue = 1
            maxValue = 12
        }

        val dayPicker = dialogView.findViewById<NumberPicker>(R.id.dayPicker).apply {
            minValue = 1
            maxValue = 31
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.select_birthday))
            .setView(dialogView)
            .setPositiveButton(R.string.ok) { _, _ ->
                val selectedYear = yearPicker.value
                val selectedMonth = monthPicker.value
                val selectedDay = dayPicker.value
                val birthday = LocalDate.of(selectedYear, selectedMonth, selectedDay)
                onPositiveClickListener(birthday)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
}
