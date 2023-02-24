package com.example.droidsoftthird

import android.animation.ValueAnimator
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.droidsoftthird.databinding.CalendarDayBinding
import com.example.droidsoftthird.databinding.FragmentScheduleRegisteredBinding
import com.example.droidsoftthird.extentions.daysOfWeekFromLocale
import com.example.droidsoftthird.extentions.setTextColorRes
import com.example.droidsoftthird.model.presentation_model.LoadState
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.yearMonth
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class ScheduleRegisteredFragment: Fragment(R.layout.fragment_schedule_registered) {

    private val viewModel: ScheduleRegisteredViewModel by viewModels()
    private val binding: FragmentScheduleRegisteredBinding by dataBinding()
    private val adapter: ScheduleEventsAdapter by lazy { ScheduleEventsAdapter(::scheduleItemClickListener) }
    private val progressDialog by lazy { //TODO Linearに変更する。
        ProgressDialog(requireActivity()).apply {
            setMessage(getString(R.string.on_progress))
            setCancelable(false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewModel.uiModel.observe(viewLifecycleOwner) { observeSchedulesState(it.schedulesLoadState) }
        viewModel.fetchAllEvents()
    }

    private fun setupView() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.weekModeCheckBox.setOnCheckedChangeListener(this@ScheduleRegisteredFragment::checkBox)
        setupWeekLabel()
        setupCalendarMatrix()
        setupEventList()
    }

    private fun setupWeekLabel() {
        binding.dayOfWeekLabel.dayOfWeekLabel.children.forEachIndexed { index, view ->
            val textView = view as TextView
            textView.text = daysOfWeekFromLocale()[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase(Locale.ENGLISH)
            textView.setTextColor(ContextCompat.getColor(textView.context, R.color.primary_text_grey))
        }
    }

    private fun setupCalendarMatrix() {
        val currentMonth = YearMonth.now()
        val startMonth = YearMonth.now().minusMonths(10)
        val endMonth = YearMonth.now().plusMonths(10)

        binding.calendarMatrix.apply {
            setup(startMonth, endMonth, daysOfWeekFromLocale().first())
            scrollToMonth(currentMonth)
            dayBinder = DayViewBinder(viewModel)
            monthScrollListener = this@ScheduleRegisteredFragment::scrollMonth //スクロール時の処理を入れてあげる。
        }
    }

    private fun setupEventList() {
        binding.recyclerView.apply {
            adapter = this@ScheduleRegisteredFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
                getDrawable(context, R.drawable.divider)?.let { setDrawable(it) } ?: throw IllegalStateException("Divider drawable not found")
            })
        }
    }

    private fun scheduleItemClickListener() {
        TODO("詳細画面への画面遷移ロジックを入れる。")
    }

    private fun observeSchedulesState(schedulesLoadState: LoadState) {
        when (schedulesLoadState) {
            is LoadState.Loading -> progressDialog.show()
            is LoadState.Loaded<*> -> { //TODO ここで受け取る型をジェネリクスで特定する。
                progressDialog.dismiss()
                adapter.submitList(viewModel.uiModel.value?.selectedEvents)
                viewModel.initializeSchedulesState()
                binding.calendarMatrix.notifyCalendarChanged()
            }
            is LoadState.Processed -> {
                adapter.submitList(viewModel.uiModel.value?.selectedEvents)
                binding.calendarMatrix.notifyMonthChanged(viewModel.uiModel.value?.selectedDate?.yearMonth ?: YearMonth.now())
            }
            is LoadState.Error -> {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), schedulesLoadState.error.toString(), Toast.LENGTH_SHORT).show()
                viewModel.initializeSchedulesState()
            }
        }
    }

    private fun scrollMonth(it: CalendarMonth) {//スクロール時のロジック
        val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
        if (binding.calendarMatrix.maxRowCount == 6) {//Monthモード
            binding.exOneYearText.text = it.yearMonth.year.toString()
            binding.exOneMonthText.text = monthTitleFormatter.format(it.yearMonth)
        } else {//Weekモード
            val firstDate = it.weekDays.first().first().date
            val lastDate = it.weekDays.last().last().date
            if (firstDate.yearMonth == lastDate.yearMonth) {
                binding.exOneYearText.text = firstDate.yearMonth.year.toString()
                binding.exOneMonthText.text = monthTitleFormatter.format(firstDate)
            } else {
                binding.exOneMonthText.text = "${monthTitleFormatter.format(firstDate)} - ${monthTitleFormatter.format(lastDate)}"
                if (firstDate.year == lastDate.year) {
                    binding.exOneYearText.text = firstDate.yearMonth.year.toString()
                } else {
                    binding.exOneYearText.text = "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                }
            }
        }
    }

    private fun checkBox (btn: CompoundButton, monthToWeek: Boolean) {
        val firstDate = binding.calendarMatrix.findFirstVisibleDay()?.date ?: return@checkBox
        val lastDate = binding.calendarMatrix.findLastVisibleDay()?.date ?: return@checkBox
        val oneWeekHeight = binding.calendarMatrix.daySize.height
        val oneMonthHeight = oneWeekHeight * 6
        val oldHeight = if (monthToWeek) oneMonthHeight else oneWeekHeight
        val newHeight = if (monthToWeek) oneWeekHeight else oneMonthHeight
        val animator = ValueAnimator.ofInt(oldHeight, newHeight)

        with(animator) {
            addUpdateListener { animator ->
                binding.calendarMatrix.updateLayoutParams {
                    height = animator.animatedValue as Int
                }
            }
            doOnStart {
                if (!monthToWeek) {
                    binding.calendarMatrix.updateMonthConfiguration(
                        inDateStyle = InDateStyle.ALL_MONTHS,
                        maxRowCount = 6,
                        hasBoundaries = true
                    )
                }
            }
            doOnEnd {
                val endMonth = YearMonth.now().plusMonths(10)
                if (monthToWeek) {
                    binding.calendarMatrix.updateMonthConfiguration(
                        inDateStyle = InDateStyle.FIRST_MONTH,
                        maxRowCount = 1,
                        hasBoundaries = false
                    )
                }
                if (monthToWeek) {
                    binding.calendarMatrix.scrollToDate(firstDate)
                } else {
                    if (firstDate.yearMonth == lastDate.yearMonth) {
                        binding.calendarMatrix.scrollToMonth(firstDate.yearMonth)
                    } else {
                        binding.calendarMatrix.scrollToMonth(minOf(firstDate.yearMonth.next, endMonth))
                    }
                }
            }
            duration = 250
            start()
        }
    }
}

class DayViewBinder(private val viewModel: ScheduleViewModel) : DayBinder<DayViewBinder.DayViewHolder> {

    override fun create(view: View) = DayViewHolder.from(view)
    override fun bind(holder: DayViewHolder, day: CalendarDay) { holder.bind(day, viewModel.uiModel.eventDates, viewModel.uiModel.selectedDate, LocalDate.now(), viewModel) }

    class DayViewHolder private constructor(view: View, dayViewBinding: CalendarDayBinding) : ViewContainer(view) {

        companion object { fun from(view: View) = DayViewHolder(view, CalendarDayBinding.bind(view)) }

        lateinit var day: CalendarDay
        lateinit var clickListener: (day: CalendarDay) -> Unit
        private var textView = dayViewBinding.calendarDayText
        private var dot = dayViewBinding.calendarDayDot

        init { view.setOnClickListener { clickListener(day) } }

        fun bind(
            day: CalendarDay,
            eventDates: List<LocalDate>,
            selectedDate: LocalDate?,
            today: LocalDate,
            viewModel: ScheduleViewModel
        ) { bindDays(day, viewModel, eventDates, selectedDate, today) }

        private fun bindDays(
            day: CalendarDay,
            viewModel: ScheduleViewModel,
            eventDates: List<LocalDate>,
            selectedDate: LocalDate?,
            today: LocalDate,
        ) {
            this.day = day
            textView.text = day.date.dayOfMonth.toString()
            clickListener = { _day -> if (_day.owner == DayOwner.THIS_MONTH) { viewModel.setSelectedDate(_day.date) } }
            if (day.owner == DayOwner.THIS_MONTH) bindThisMonthDays(eventDates, day, selectedDate, today) else bindOtherMonthDays()
        }

        private fun bindThisMonthDays(
            eventDates: List<LocalDate>,
            day: CalendarDay,
            selectedDate: LocalDate?,
            today: LocalDate,
        ) {
            dot.isVisible = eventDates.contains(day.date)
            when {
                selectedDate == day.date -> { textView.setBackgroundResource(R.drawable.ic_baseline_circle_selected) }
                today == day.date -> { textView.setBackgroundResource(R.drawable.ic_baseline_circle_today) }
                else -> {
                    textView.setTextColorRes(R.color.primary_text_grey_dark)
                    textView.background = null
                }
            }
        }

        private fun bindOtherMonthDays() {
            textView.setTextColorRes(R.color.primary_accent_red)
            dot.isVisible = false
            textView.background = null
        }
    }
}
