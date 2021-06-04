package com.example.droidsoftthird

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.droidsoftthird.databinding.CalendarDayLayoutBinding
import com.example.droidsoftthird.databinding.CalendarHeaderBinding
import com.example.droidsoftthird.databinding.FragmentScheduleApprovedBinding
import com.example.droidsoftthird.databinding.FragmentScheduleBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ApprovedScheduleFragment : Fragment() {

    @Inject
    lateinit var approvedScheduleViewModelAssistedFactory: ApprovedScheduleViewModel.Factory
    //private var groupId:String = "ALL"
    private lateinit var viewModel: ApprovedScheduleViewModel
    private lateinit var binding:FragmentScheduleApprovedBinding


    //TODO 1.クリックされた時の処理を記入
    private var selectedDate: LocalDate? = null
    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    private val today = LocalDate.now()

    companion object {
        fun newInstance() = ApprovedScheduleFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



    //TODO ------STEP0 Injectionなどの設定
    //  画面遷移後、親のフラグメントに代入し、子フラグメン間で画面遷移があったとしても、問題ないよう常にそこを参照し続ける。
    //  一方で、ViewPagerのライフサイクルの中でGroupIdを生存させる方法があれば、そちらを採用する。

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_schedule_approved, container, false)

        groupId = GroupDetailFragmentArgs.fromBundle(requireArguments()).groupId
        viewModel = approvedScheduleViewModelAssistedFactory.create(groupId)


    //TODO -----STEP1 DayViewContainer(ViewHolder)のクラス設置

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = CalendarDayLayoutBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {//当該月の日付だった場合、SelectDateメソッド
                        selectDate(day.date)
                    }
                }
            }
        }


    //TODO -----STEP2 DayViewContainerを操作するDayBinderを定義

        binding.calendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)//ViewHolderを生成
            override fun bind(container: DayViewContainer, day: CalendarDay) {//生成したDayViewに対してBind処理をかけていく。

                container.day = day// Set the calendar day for this container.
                val textView = container.binding.dayText
                val dotView = container.binding.dotView
                textView.text = day.date.dayOfMonth.toString()// Set the date text

                if (day.owner == DayOwner.THIS_MONTH) {//同月の処理だった場合、
                    textView.makeVisible()//表示し、
                    when (day.date) {
                        today -> {//今日だった場合
                            textView.setTextColorRes(R.color.white_first)//Appearanceを変更し、
                            textView.setBackgroundResource(R.drawable.today_bg)
                            dotView.makeInVisible()
                        }
                        selectedDate -> {//選択日だった場合、
                            textView.setTextColorRes(R.color.blue_first)//Appearanceを変更し
                            textView.setBackgroundResource(R.drawable.selected_bg)
                            dotView.makeInVisible()
                        }
                        else -> {//その他、通常のAppearance
                            textView.setTextColorRes(R.color.black_first)
                            textView.background = null
                            dotView.isVisible = schedules[day.date].orEmpty().isNotEmpty()//イベントが入っている場合、ドットを表示
                        }
                    }
                } else {//それ以外の月は非表示
                    textView.makeInVisible()
                    dotView.makeInVisible()
                }
            }
        }

    //TODO -----STEP3 CalendarViewをセットアップ

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()


        /*TODO 削除対象

        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        */

        binding.calendar.apply {
            setup(
                currentMonth.minusMonths(10),//開始月を設定
                currentMonth.plusMonths(10),//終了月を設定
                daysOfWeek.first()//週の最初の日を設定
            )
            scrollToMonth(currentMonth)//現在の月まで移動。
        }

        if (savedInstanceState == null) {
            binding.calendar.post {
                // Show today's events initially.
                selectDate(today)
            }
        }

        binding.calendar.monthScrollListener = {

            /*TODO ページ移動するたびに月の表示を切り替える処理を行う。

            homeActivityToolbar.title = if (it.year == today.year) {
                titleSameYearFormatter.format(it.yearMonth)
            } else {
                titleFormatter.format(it.yearMonth)
            }                                                   */

            // Select the first day of the month when we scroll to a new month.
            // 月が更新された際に1stを選択する。
            selectDate(it.yearMonth.atDay(1))
        }


    //TODO -----STEP4 MonthHeaderを生成

        //TODO MonthHeaderについては、年と月を入れて表示に切り替える。
        //      SAMPLE1を参照とする。


        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root//Rootを取得
        }

        binding.calendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }.forEachIndexed { index, textView ->
                        textView.text = daysOfWeek[index].name.first().toString()
                        textView.setTextColorRes(R.color.black_first)
                    }
                }
            }
        }

    //TODO -----STEP5 FloatingButtonの設置

        //TODO 関心の分離のため、Eventクラスを用いてクリックイベントをViewModelに移行する。
        //      GroupIdの保存場所によって（親か子フラグメントか）FloatingButtonの設置場所もかわる。

        //OnCreate時に
        /*

            binding.floatingActionButton.setOnClickListener(View.OnClickListener { v ->
            val action: NavDirections =
                HomeFragmentDirections.actionHomeFragmentToAddEventFragment()
            Navigation.findNavController(v).navigate(action)
        })*/

    //TODO -----STEP5 ScheduleRecyclerを設定

        val adapter = ScheduleAdapter(ScheduleListener{ date ->
            viewModel.onScheduleClicked(date)
        })

        binding.scheduleRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.scheduleRecycler.adapter = adapter
        binding.scheduleRecycler.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))


        viewModel.getMySchedules()

        viewModel.schedules.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.let {
                adapter.submitList(it) }
        })


        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date

            oldDate?.let { binding.calendar.notifyDateChanged(it) } //カレンダーをリロード(古い日付)　
            binding.calendar.notifyDateChanged(date) //カレンダーをリロード(新しい日付)　状態の遷移を表す。
            updateAdapterForDate(date) //AdapterをUpdateする。
        }
    }

    private fun updateAdapterForDate(date: LocalDate) {//選択される日付が変更されるごとに日付をアップデートする。
        viewModel.apply {
            getMySchedules(date)
            events.addAll(this@ApprovedScheduleFragment.events[date].orEmpty())
            notifyDataSetChanged()
        }
        binding.selectedDateText.text = selectionFormatter.format(date)//表示される日付の変更
    }


}


/**
 *

// ActivityToolBarに日付を設定

    homeActivityToolbar.title = if (it.year == today.year) {
    titleSameYearFormatter.format(it.yearMonth)
    } else {
    titleFormatter.format(it.yearMonth)
    }


 */




