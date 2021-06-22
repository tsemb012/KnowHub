package com.example.droidsoftthird.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.CreateScheduleViewModel
import com.example.droidsoftthird.R
import java.util.*

class TimePickerEndFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val viewModel: CreateScheduleViewModel by viewModels({ requireParentFragment() })
    private var startHour: Int? = -1
    private var startMinute: Int? = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        startHour = arguments?.getInt("start_hour")
        startMinute = arguments?.getInt("start_minute")

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, R.string.end_time, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        if (startHour!=-1&&startMinute!=-1){
        viewModel.postStartTime(startHour,startMinute)
        viewModel.postEndTime(hourOfDay, minute)
        }
    }
}