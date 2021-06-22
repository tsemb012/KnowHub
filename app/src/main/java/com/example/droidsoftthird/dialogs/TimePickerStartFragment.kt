package com.example.droidsoftthird.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.CreateProfileViewModel
import com.example.droidsoftthird.CreateScheduleViewModel
import com.example.droidsoftthird.R
import java.text.DateFormat
import java.util.*

class TimePickerStartFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val viewModel: CreateScheduleViewModel by viewModels({ requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity,R.string.start_time, this, hour, minute, is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val dialog_next = TimePickerEndFragment()
        val args = Bundle()
        args.putInt("start_hour", hourOfDay)
        args.putInt("start_minute", minute)
        dialog_next.arguments = args
        dialog_next.show(
            parentFragmentManager,
            "time_picker_end"
        );
    }
}