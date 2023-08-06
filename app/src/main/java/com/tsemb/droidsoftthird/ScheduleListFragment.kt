package com.tsemb.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.droidsoftthird.ScheduleHomeFragmentDirections
import com.tsemb.droidsoftthird.composable.event.EventListScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ScheduleListFragment:Fragment()  {

    private val viewModel: ScheduleListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchAllEvents()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    EventListScreen(viewModel, ::selectEvent)
                }
            }
        }
    }

    private fun selectEvent(eventId: String) {
        findNavController().navigate(
            ScheduleHomeFragmentDirections.actionScheduleHomeFragmentToScheduleDetailFragment(
                eventId
            )
        )
    }
}