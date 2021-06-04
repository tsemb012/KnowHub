package com.example.droidsoftthird

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ScheduleDetailFragment : Fragment() {

    companion object {
        fun newInstance() = ScheduleDetailFragment()
    }

    private lateinit var viewModel: ScheduleDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.schedule_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScheduleDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}