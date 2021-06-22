package com.example.droidsoftthird

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
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
    private lateinit var viewModel: ApprovedScheduleViewModel
    private lateinit var binding:FragmentScheduleApprovedBinding


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
        binding.lifecycleOwner = viewLifecycleOwner


    //TODO -----STEP1 DayViewContainer(ViewHolder)のクラス設置

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = CalendarDayLayoutBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {//当該月の日付だった場合、SelectDateメソッド
                        viewModel.selectDate(day.date)
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
                        LocalDate.now() -> {//今日だった場合
                            textView.setTextColorRes(R.color.white_first)//Appearanceを変更し、
                            textView.setBackgroundResource(R.drawable.today_bg)
                            dotView.makeInVisible()
                        }
                        viewModel.selectedDate.value -> {//選択日だった場合、
                            textView.setTextColorRes(R.color.blue_first)//Appearanceを変更し
                            textView.setBackgroundResource(R.drawable.selected_bg)
                            dotView.makeInVisible()
                        }
                        else -> {//その他、通常のAppearance
                            textView.setTextColorRes(R.color.black_first)
                            textView.background = null
                            dotView.isVisible = viewModel.events.value?.get(day.date).orEmpty().isNotEmpty()//イベントが入っている場合、ドットを表示
                        }
                    }
                } else {//それ以外の月は非表示
                    textView.makeInVisible()
                    dotView.makeInVisible()
                }
            }
        }

    //TODO -----STEP3 CalendarViewをセットアップ

        binding.calendar.apply {
            setup(
                YearMonth.now().minusMonths(10),//開始月を設定
                YearMonth.now().plusMonths(10),//終了月を設定
                daysOfWeekFromLocale().first()//週の最初の日を設定
            )
            scrollToMonth(YearMonth.now())//現在の月まで移動。
        }

        if (savedInstanceState == null) {
            binding.calendar.post {
                // Show today's events initially.
                viewModel.selectDate(LocalDate.now())
            }
        }

        binding.calendar.monthScrollListener = {

            binding.yearAndMonth.text = DateTimeFormatter.ofPattern("MMM yyyy").format(it.yearMonth)

            viewModel.selectDate(it.yearMonth.atDay(1))
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
                        textView.text = daysOfWeekFromLocale()[index].name.first().toString()
                        textView.setTextColorRes(R.color.black_first)
                    }
                }
            }
        }

    //TODO -----STEP5 Spinnerの設置

        /*  viewModel.groups.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let{
                val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,it)
                binding.spinner.adapter = adapter}
        })*/
        //TODO DataBindingを使用し、Attributeのentriesからリストを保存。不具合がないようであれば、上記のコード削除する。




    //TODO -----STEP6 FloatingButtonの設置

        //TODO 関心の分離のため、Eventクラスを用いてクリックイベントをViewModelに移行する。
        //      GroupIdの保存場所によって（親か子フラグメントか）FloatingButtonの設置場所もかわる。

        //OnCreate時に
        /*

            binding.floatingActionButton.setOnClickListener(View.OnClickListener { v ->
            val action: NavDirections =
                HomeFragmentDirections.actionHomeFragmentToAddEventFragment()
            Navigation.findNavController(v).navigate(action)
        })

        */

    //TODO -----STEP7 ScheduleRecyclerを設定

        val adapter = ScheduleAdapter(ScheduleListener{ scheduleId ->
            viewModel.onScheduleClicked(scheduleId)
        })

        viewModel.navigateToScheduleDetail.observe(viewLifecycleOwner,EventObserver{
            findNavController().navigate(
                ApprovedScheduleFragmentDirections.actionScheduleFragmentToScheduleDetailFragment(it))
        })

        binding.scheduleRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.scheduleRecycler.adapter = adapter
        binding.scheduleRecycler.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))

        viewModel.schedules.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()}
        })

        viewModel.oldDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                binding.calendar.notifyDateChanged(it)  }
        })//カレンダーをリロード(古い日付)　

        viewModel.selectedDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                binding.calendar.notifyDateChanged(it)
                binding.selectedDateText.text = DateTimeFormatter.ofPattern("d MMM yyyy").format(it)}
        })//カレンダーをリロード(新しい日付)

        return binding.root
    }
}





