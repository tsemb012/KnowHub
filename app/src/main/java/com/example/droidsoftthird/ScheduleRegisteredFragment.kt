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
import com.example.droidsoftthird.databinding.FragmentScheduleRegisteredBinding
import com.example.droidsoftthird.extentions.daysOfWeekFromLocale
import com.example.droidsoftthird.extentions.setTextColorRes
import com.example.droidsoftthird.model.LoadState
import com.example.droidsoftthird.model.SchedulePlan
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.ViewContainer
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleRegisteredFragment: Fragment(R.layout.fragment_schedule_registered) {

   // @Inject lateinit var scheduleRegisteredViewModelAssistedFactory: ScheduleRegisteredViewModel.Factory
    private val viewModel: ScheduleRegisteredViewModel by viewModels()
    private val binding: FragmentScheduleRegisteredBinding by dataBinding()
    private val daysOfWeek:Array<DayOfWeek> by lazy { daysOfWeekFromLocale() }
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

        /*class DayViewContainer(view: View): ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = FragmentScheduleRegisteredBinding.bind(view)

        }*/


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


}
