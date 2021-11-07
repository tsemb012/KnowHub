package com.example.droidsoftthird

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.databinding.FragmentScheduleRegisteredBinding
import com.example.droidsoftthird.model.LoadState
import com.example.droidsoftthird.model.SchedulePlan
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.ViewContainer
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleRegisteredFragment: Fragment(R.layout.fragment_schedule_registered) {

   // @Inject lateinit var scheduleRegisteredViewModelAssistedFactory: ScheduleRegisteredViewModel.Factory
    private val viewModel: ScheduleRegisteredViewModel by viewModels()
    private val binding: FragmentScheduleRegisteredBinding by dataBinding()
    private val progressDialog by lazy {
        ProgressDialog(requireActivity()).apply {
            setMessage(getString(R.string.on_progress))
            setCancelable(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
         viewModel.uiModel.observe(viewLifecycleOwner){
             observeSchedulesState(it.schedulesLoadState)
         }

        class DayViewContainer(view: View): ViewContainer(view) {
            lateinit var day: CalendarDay

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


}
