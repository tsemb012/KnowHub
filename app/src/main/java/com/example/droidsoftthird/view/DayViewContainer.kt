package com.example.droidsoftthird.view

import android.view.View
import com.example.droidsoftthird.databinding.CalendarDayBinding
import com.example.droidsoftthird.databinding.CalendarDayLegendBinding
import com.example.droidsoftthird.databinding.FragmentScheduleRegisteredBinding
import com.example.droidsoftthird.databinding.FragmentScheduleRegisteredBindingImpl
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer

/*
class DayViewContainer(view: View): ViewContainer(view) {
    lateinit var day: CalendarDay
    val textView = CalendarDayBinding.bind(view).oneDayText
    init {
        view.setOnClickListener {
            if (day.owner == DayOwner.THIS_MONTH) {//選択した月が当月にあたるかを確認。
                if (selectedDates.)
            }
        }
    }
*/

/*

val textView = Example1CalendarDayBinding.bind(view).exOneDayText
init {
    view.setOnClickListener {
        if (day.owner == DayOwner.THIS_MONTH) {
            if (selectedDates.contains(day.date)) {
                selectedDates.remove(day.date)
            } else {
                selectedDates.add(day.date)
            }
            binding.exOneCalendar.notifyDayChanged(day)
        }
    }
}

*/
