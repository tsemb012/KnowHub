package com.example.droidsoftthird

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.databinding.CalendarDayBinding
import com.example.droidsoftthird.databinding.FragmentScheduleRegisteredBinding
import com.example.droidsoftthird.extentions.daysOfWeekFromLocale
import com.example.droidsoftthird.extentions.setTextColorRes
import com.example.droidsoftthird.model.LoadState
import com.example.droidsoftthird.model.SchedulePlan
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleRegisteredFragment: Fragment(R.layout.fragment_schedule_registered) {

   // @Inject lateinit var scheduleRegisteredViewModelAssistedFactory: ScheduleRegisteredViewModel.Factory
    private val viewModel: ScheduleRegisteredViewModel by viewModels()
    private val binding: FragmentScheduleRegisteredBinding by dataBinding()
    private val daysOfWeek:Array<DayOfWeek> by lazy { daysOfWeekFromLocale() }
    private val selectedDates = mutableSetOf<LocalDate>()//TODO ViewModelに持っていく変数
    private val today = LocalDate.now()//TODO FragmentでもViewModelでもどっちでも良いな。
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")//TODO Extensionsにうつしても良いかも。
    private val currentMonth = YearMonth.now()
    private val startMonth = currentMonth.minusMonths(10)
    private val endMonth = currentMonth.plusMonths(10)
    private val progressDialog by lazy {
        ProgressDialog(requireActivity()).apply {
            setMessage(getString(R.string.on_progress))
            setCancelable(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchAllSchedules()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.uiModel.observe(viewLifecycleOwner){ observeSchedulesState(it.schedulesLoadState) }
        binding.legendLayout.legendLayout.children.forEachIndexed { index, view -> //親レイアウトからinclude、元layout、そして子ビューを引き出して処理。
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase(Locale.ENGLISH)
                setTextColorRes(R.color.primary_text_grey)
            }
        }
        with(binding.oneCalendar){
            setup(startMonth, endMonth, daysOfWeek.first())
            scrollToMonth(currentMonth)
            dayBinder = DayBinderImpl()//リサイクラービューにビューホルダーを入れるようなイメージ？
            monthScrollListener = this@ScheduleRegisteredFragment::scrollMonth //スクロール時の処理を入れてあげる。
        }
    }

    private fun observeSchedulesState(state: LoadState) {
        when (state) {
            is LoadState.Loading -> progressDialog.show()
            is LoadState.Loaded<*> -> {
                progressDialog.dismiss()
                //TODO ここに成功時の処理を記述する。
                viewModel.initializeSchedulesState()
            }
            is LoadState.Error -> {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), state.error.toString(), Toast.LENGTH_SHORT).show()
                viewModel.initializeSchedulesState()
            }
        }
    }

    private fun showEditTextDialog(state: LoadState.Loaded<*>) {

    }

    fun scrollMonth(it: CalendarMonth) {
        if (binding.oneCalendar.maxRowCount == 6) {//基本はこっち、下はWeekModeなので、消す方法を逆に考える。
            binding.exOneYearText.text = it.yearMonth.year.toString()
            binding.exOneMonthText.text = monthTitleFormatter.format(it.yearMonth)
        } else {
            // In week mode, we show the header a bit differently.
            // We show indices with dates from different months since
            // dates overflow and cells in one index can belong to different
            // months/years.
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

    inner class DayViewContainer(view: View) : ViewContainer(view) {//TODO 可能であれば外にクラスを出して疎結合にしておきたい。
        // Will be set when this container is bound. See the dayBinder.
        lateinit var day: CalendarDay//原理はわからないが、押した日が反映されている。
        val textView = CalendarDayBinding.bind(view).exOneDayText
        init {
            view.setOnClickListener {//複数日ではなく、1日だけ洗濯したい時は、ここのロジックとselectedDatesを切り替えればいけそう。
                if (day.owner == DayOwner.THIS_MONTH) {
                    if (selectedDates.contains(day.date)) {
                        selectedDates.remove(day.date)
                    } else {
                        selectedDates.add(day.date)
                    }
                    binding.oneCalendar.notifyDayChanged(day)
                }
            }
        }
    }

    inner class DayBinderImpl(): DayBinder<DayViewContainer> { //TODO　疎結合にする必要がある。リサイクラービューのやり方を真似れば大丈夫。 　
        override fun create(view: View): DayViewContainer = DayViewContainer(view)
        override fun bind(container: DayViewContainer, day: CalendarDay) {
            container.day = day
            container.textView.text = day.date.dayOfMonth.toString()
            if (day.owner == DayOwner.THIS_MONTH) {
                when {
                    selectedDates.contains(day.date) -> {
                        container.textView.setTextColorRes(R.color.primary_text_grey)
                        container.textView.setBackgroundResource(R.drawable.radius_rectangular_prime_base_yellow)
                    }
                    today == day.date -> {
                        container.textView.setTextColorRes(R.color.primary_accent_yellow)
                        container.textView.setBackgroundResource(R.drawable.radius_rectangular_prime_base_yellow)
                    }
                    else -> {
                        container.textView.setTextColorRes(R.color.primary_text_grey_dark)
                        container.textView.background = null
                    }
                }
            } else {
                container.textView.setTextColorRes(R.color.primary_white)
                container.textView.background = null
            }
        }
    }
}

//TODO 予定に関しては、全件取得で良いと思う。
/*
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    when {
                        selectedDates.contains(day.date) -> {
                            textView.setTextColorRes(R.color.example_1_bg)
                            textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                        }
                        today == day.date -> {
                            textView.setTextColorRes(R.color.example_1_white)
                            textView.setBackgroundResource(R.drawable.example_1_today_bg)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.example_1_white)
                            textView.background = null
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.example_1_white_light)
                    textView.background = null
                }
            }*/

