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
import com.example.droidsoftthird.extentions.gd
import com.example.droidsoftthird.extentions.setTextColorRes
import com.example.droidsoftthird.model.LoadState
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

   // @Inject lateinit var scheduleRegisteredViewModelAssistedFactory: ScheduleRegisteredViewModel.Factory
    private val viewModel: ScheduleRegisteredViewModel by viewModels()
    private val binding: FragmentScheduleRegisteredBinding by dataBinding()
    private val adapter: ScheduleEventsAdapter by lazy { ScheduleEventsAdapter(::scheduleItemClickListener) }
    private val daysOfWeek:Array<DayOfWeek> by lazy { daysOfWeekFromLocale() }
    private val currentMonth = YearMonth.now()
    private val startMonth = currentMonth.minusMonths(10)
    private val endMonth = currentMonth.plusMonths(10)
    private val progressDialog by lazy {
        ProgressDialog(requireActivity()).apply {
            setMessage(getString(R.string.on_progress))
            setCancelable(false)
        }
    }
    //TODO ScheduleEventsAdapterをHiltでインジェクトする。

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchAllSchedules()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            lifecycleOwner = viewLifecycleOwner

            legendLayout.legendLayout.children.forEachIndexed { index, view -> //親レイアウトからinclude、元layout、そして子ビューを引き出して処理。
                (view as TextView).apply {
                    text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                        .toUpperCase(Locale.ENGLISH)
                    setTextColorRes(R.color.primary_text_grey)
                }
            }

            oneCalendar.apply {
                setup(startMonth, endMonth, daysOfWeek.first())
                scrollToMonth(currentMonth)
                dayBinder = DayBinderImpl(viewModel)//リサイクラービューにビューホルダーを入れるようなイメージ？
                monthScrollListener =
                    this@ScheduleRegisteredFragment::scrollMonth //スクロール時の処理を入れてあげる。
            }

            weekModeCheckBox.setOnCheckedChangeListener(this@ScheduleRegisteredFragment::checkBox)

            recyclerView.apply {
                adapter = this@ScheduleRegisteredFragment.adapter
                layoutManager = LinearLayoutManager(context)
                val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager(context).orientation)
                dividerItemDecoration.setDrawable(getDrawable(context, R.drawable.divider) ?: throw IllegalStateException() )
                addItemDecoration(dividerItemDecoration)
            }
        }
        viewModel.uiModel.observe(viewLifecycleOwner) { observeSchedulesState(it.schedulesLoadState) }
    }

    private fun scheduleItemClickListener() {
        TODO("詳細画面への画面遷移ロジックを入れる。")
    }

    private fun observeSchedulesState(schedulesLoadState: LoadState) {
        when (schedulesLoadState) {
            is LoadState.Loading -> progressDialog//.show()
            is LoadState.Loaded<*> -> { //TODO ここで受け取る型をジェネリクスで特定する。
                progressDialog.dismiss()
                adapter.submitList(viewModel.uiModel.value?.selectedEvents)
                viewModel.initializeSchedulesState()
            }
            is LoadState.Processed -> {
                adapter.submitList(viewModel.uiModel.value?.selectedEvents)
                //binding.oneCalendar.notifyMonthChanged(viewModel.uiModel.value?.selectedDate?.yearMonth ?: YearMonth.now())
                //binding.oneCalendar.notifyDateChanged(viewModel.uiModel.value?.selectedDate?: LocalDate.now(),DayOwner.THIS_MONTH)
                //binding.oneCalendar.dayBinder = DayBinderImpl(viewModel)
                //viewModel.initializeSchedulesState()

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
        if (binding.oneCalendar.maxRowCount == 6) {//Monthモード
            binding.exOneYearText.text = it.yearMonth.year.toString()
            binding.exOneMonthText.text = monthTitleFormatter.format(it.yearMonth)
        } else {//Weekモード
            val firstDate = it.weekDays.first().first().date
            val lastDate = it.weekDays.last().last().date
            if (firstDate.yearMonth == lastDate.yearMonth) {
                binding.exOneYearText.text = firstDate.yearMonth.year.toString()
                binding.exOneMonthText.text = monthTitleFormatter.format(firstDate)
            } else {
                binding.exOneMonthText.text =
                    "${monthTitleFormatter.format(firstDate)} - ${monthTitleFormatter.format(lastDate)}"
                if (firstDate.year == lastDate.year) {
                    binding.exOneYearText.text = firstDate.yearMonth.year.toString()
                } else {
                    binding.exOneYearText.text = "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                }
            }
        }
    }

    private fun checkBox (btn: CompoundButton, monthToWeek: Boolean) {
        val firstDate = binding.oneCalendar.findFirstVisibleDay()?.date ?: return@checkBox
        val lastDate = binding.oneCalendar.findLastVisibleDay()?.date ?: return@checkBox
        val oneWeekHeight = binding.oneCalendar.daySize.height
        val oneMonthHeight = oneWeekHeight * 6
        val oldHeight = if (monthToWeek) oneMonthHeight else oneWeekHeight
        val newHeight = if (monthToWeek) oneWeekHeight else oneMonthHeight
        val animator = ValueAnimator.ofInt(oldHeight, newHeight)

        with(animator) {
            addUpdateListener { animator ->
                binding.oneCalendar.updateLayoutParams {
                    height = animator.animatedValue as Int
                }
            }
            doOnStart {
                if (!monthToWeek) {
                    binding.oneCalendar.updateMonthConfiguration(
                        inDateStyle = InDateStyle.ALL_MONTHS,
                        maxRowCount = 6,
                        hasBoundaries = true
                    )
                }
            }
            doOnEnd {
                if (monthToWeek) {
                    binding.oneCalendar.updateMonthConfiguration(
                        inDateStyle = InDateStyle.FIRST_MONTH,
                        maxRowCount = 1,
                        hasBoundaries = false
                    )
                }
                if (monthToWeek) {
                    binding.oneCalendar.scrollToDate(firstDate)
                } else {
                    if (firstDate.yearMonth == lastDate.yearMonth) {
                        binding.oneCalendar.scrollToMonth(firstDate.yearMonth)
                    } else {
                        binding.oneCalendar.scrollToMonth(minOf(firstDate.yearMonth.next, endMonth))
                    }
                }
            }
            duration = 250
            start()
        }
    }

    class DayBinderImpl(val viewModel: ScheduleViewModel) : DayBinder<DayBinderImpl.DayViewHolder> { //TODO　疎結合にする必要がある。リサイクラービューのやり方を真似れば大丈夫。 　
        private val today = LocalDate.now()

        override fun create(view: View): DayViewHolder = DayViewHolder.from(view)
        override fun bind(holder: DayViewHolder, day: CalendarDay) {

            holder.day = day
            holder.textView.text = day.date.dayOfMonth.toString()
            holder.clickListener = { _day -> if (_day.owner == DayOwner.THIS_MONTH) {
                viewModel.setSelectedDate(_day.date)
            } }

            if (day.owner != DayOwner.THIS_MONTH) {
                holder.textView.setTextColorRes(R.color.primary_white)
                holder.textView.background = null
                holder.dot.isVisible = false
            } else {
                if (!viewModel.uiModel.eventDates.contains(day.date)) { holder.dot.isVisible = false }
                when {
                    day.date == today -> { holder.textView.setBackgroundResource(R.drawable.ic_baseline_circle_today) }
                    viewModel.uiModel.selectedDate == day.date -> { holder.textView.background = holder.view.gd(R.drawable.ic_baseline_circle_selected) }
                    else -> { holder.textView.background = null }
                }
            }
        }

        class DayViewHolder private constructor(view: View, dayViewBinding: CalendarDayBinding) : ViewContainer(view) {

            lateinit var day: CalendarDay
            lateinit var clickListener: (day : CalendarDay) -> Unit
            var textView = dayViewBinding.calendarDayText
            var dot = dayViewBinding.calendarDayDot
            init { view.setOnClickListener { clickListener(day) } }

            companion object {
                fun from(view: View): DayViewHolder {
                    val binding = CalendarDayBinding.bind(view)
                    return DayViewHolder(view, binding)
                }
            }
        }
    }
}
