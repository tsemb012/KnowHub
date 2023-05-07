package com.example.droidsoftthird

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.droidsoftthird.databinding.FragmentScheduleHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //このActivityはHiltが使うと宣言し、依存関係をHiltから引っ張ってくる。
class ScheduleHomeFragment: Fragment(R.layout.fragment_schedule_home) {

    private val binding: FragmentScheduleHomeBinding by dataBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            pager.apply {
                pager.adapter = ScheduleHomePagerAdapter(this@ScheduleHomeFragment)
                pager.isUserInputEnabled = false
            }
            tabLayout.let {
                TabLayoutMediator(it, pager) { tab, position -> tab.text = "OBJECT" + (position + 1) }.attach()
                it.getTabAt(0)?.setText(R.string.schedule_calendar) ?: throw IllegalStateException()
                it.getTabAt(1)?.setText(R.string.schedule_list) ?: throw IllegalStateException()
            }
            floatingActionButton.setOnClickListener {
                val action = ScheduleHomeFragmentDirections.actionScheduleHomeFragmentToScheduleCreateFragment()
                Navigation.findNavController(it).navigate(action)
            }
        }
    }
}