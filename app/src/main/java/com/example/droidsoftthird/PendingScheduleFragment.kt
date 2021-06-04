package com.example.droidsoftthird

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class PendingScheduleFragment : Fragment() {

    companion object {
        fun newInstance() = PendingScheduleFragment()
    }

    private lateinit var viewModel: PendingScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.pending_schedule_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PendingScheduleViewModel::class.java)
        // TODO: Use the ViewModel
    }

}