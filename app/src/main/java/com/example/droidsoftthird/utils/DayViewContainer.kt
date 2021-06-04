package com.example.droidsoftthird.utils

import android.view.View
import android.widget.TextView
import com.example.droidsoftthird.R
import com.example.droidsoftthird.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = CalendarDayLayoutBinding.bind(view).calendarDayText



}