package com.example.droidsoftthird

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.droidsoftthird.composable.event.EventListItem
import com.example.droidsoftthird.composable.shared.BoldTitleItem
import com.example.droidsoftthird.composable.shared.CommonLinearProgressIndicator
import com.example.droidsoftthird.composable.shared.EmptyMessage
import com.example.droidsoftthird.composable.shared.FundamentalSheet
import com.example.droidsoftthird.databinding.CalendarDayBinding
import com.example.droidsoftthird.databinding.FragmentScheduleCalendarBinding
import com.example.droidsoftthird.extentions.daysOfWeekFromLocale
import com.example.droidsoftthird.extentions.setTextColorRes
import com.example.droidsoftthird.model.domain_model.ItemEvent
import com.example.droidsoftthird.model.domain_model.SimpleGroup
import com.example.droidsoftthird.model.presentation_model.NotifyType
import com.example.droidsoftthird.model.presentation_model.eventDates
import com.example.droidsoftthird.model.presentation_model.selectedDate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class ScheduleCalendarFragment: Fragment(R.layout.fragment_schedule_calendar) {

    private val viewModel: ScheduleCalendarViewModel by activityViewModels()
    private val binding: FragmentScheduleCalendarBinding by dataBinding()
    private val adapter: ScheduleEventsAdapter by lazy { ScheduleEventsAdapter(::selectEvent) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("key") { _, bundle ->
            val result = bundle.getString("result")
            viewModel.setSelectedGroupId(result ?: "")
            Log.d("groupId_tsemb012", bundle.getString("groupId") ?: "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchAllEvents()
        viewModel.fetchSimpleGroups()
        setupView()
        bindUiModel()
    }


    private fun setupView() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setupGroupDialog()
        setupWeekLabel()
        setupCalendarMatrix()
        //setupEventList()
    }

    private fun bindUiModel() {
        viewModel.uiModel.observe(viewLifecycleOwner) {
            it.error?.let { error ->
                Toast.makeText(
                    requireContext(),
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            adapter.submitList(it.selectedEvents)
            when (it.notifyType) {
                NotifyType.ALL -> binding.calendarMatrix.notifyCalendarChanged()
                NotifyType.SELECTED -> binding.calendarMatrix.notifyMonthChanged(it.selectedDate.yearMonth)
                NotifyType.NONE -> Unit
            }

            binding.selectGroupButton.text = it.selectedSimpleGroup?.groupName ?: getString(R.string.select_group)

            binding.topComposeView.setContent {
                MyProgressBar(viewModel.uiModel.value?.isLoading == true)
            }

            binding.bottomComposeView.setContent {
                FundamentalSheet(
                    content = {
                              EventListContent(
                                  selectedDate = it.selectedDate.toString(),
                                  events = it.selectedEvents,
                                  navigate = {  },
                                  isLoading = it.isLoading,
                              )
                    },
                    isLoading = false,
                    error = viewModel.uiModel.value?.error
                )
                /*Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                    CommonAddButton(
                        label = "イベントを追加",
                        navigate = { findNavController().navigate(ScheduleHomeFragmentDirections.actionScheduleHomeFragmentToScheduleCreateFragment()) },
                        modifier = Modifier
                            .padding(bottom = 32.dp, end = 16.dp)
                            .align(Alignment.BottomEnd)
                    )
                }*/
            }
        }
    }


    @Composable
    private fun EventListContent(
        selectedDate: String,
        isLoading: Boolean,
        events: List<ItemEvent>?,
        navigate: (String) -> Unit,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {

            item { BoldTitleItem(selectedDate, Modifier.padding(bottom = 8.dp)) }
            if (!isLoading && events?.isEmpty() == true) item { EmptyMessage(R.string.join_or_create_group) }
            events?.let { list ->
                items(list.size) { EventListItem(events[it], navigate) }
            }

        }
    }


    private fun setupWeekLabel() {
        binding.dayOfWeekLabel.dayOfWeekLabel.children.forEachIndexed { index, view ->
            val textView = view as TextView
            textView.text = daysOfWeekFromLocale()[index].getDisplayName(TextStyle.SHORT, Locale.JAPANESE).toUpperCase(Locale.ENGLISH)
            textView.setTextColor(ContextCompat.getColor(textView.context, R.color.gray))
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
            monthScrollListener = this@ScheduleCalendarFragment::scrollMonth
        }
    }

    /*private fun setupEventList() {
        binding.recyclerView.apply {
            adapter = this@ScheduleCalendarFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
                getDrawable(context, R.drawable.divider)?.let { setDrawable(it) } ?: throw IllegalStateException("Divider drawable not found")
            })
        }
    }*/

    private fun selectEvent(eventId: String) {
        findNavController().navigate(ScheduleHomeFragmentDirections.actionScheduleHomeFragmentToScheduleDetailFragment(eventId))
    }

    private fun scrollMonth(it: CalendarMonth) {//スクロール時のロジック
        val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
        if (binding.calendarMatrix.maxRowCount == 6) {//Monthモード
            binding.exOneYearText.text = "${it.yearMonth.year}年"
            binding.exOneMonthText.text = monthTitleFormatter.format(it.yearMonth)
        } else {//Weekモード
            val firstDate = it.weekDays.first().first().date
            val lastDate = it.weekDays.last().last().date
            if (firstDate.yearMonth == lastDate.yearMonth) {
                binding.exOneYearText.text = "${firstDate.yearMonth.year}年"
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
    private fun FragmentScheduleCalendarBinding.setupGroupDialog() {
        selectGroupButton.setOnClickListener {
            val simpleGroups = viewModel.uiModel.value?.simpleGroups ?: emptyList()
            val groups = listOf(SimpleGroup("", getString(R.string.non_selected))) + simpleGroups
            val groupNames = groups.map { it.groupName }

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.show_group_asscociated_events))
                .setItems(groupNames.toTypedArray()) { _, which ->
                    val selectedGroupId = groups[which].groupId ?: ""
                    viewModel.setSelectedGroupId(selectedGroupId)
                }
                .show()
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
            textView.setTextColorRes(R.color.base_100)
            dot.isVisible = false
            textView.background = null
        }
    }
}

@Composable
fun MyProgressBar(loading: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (loading) CommonLinearProgressIndicator() else Spacer(modifier = Modifier.height(4.dp))
        Spacer(modifier = Modifier.height(8.dp))
    }
}
