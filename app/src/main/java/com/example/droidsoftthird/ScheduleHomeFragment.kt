package com.example.droidsoftthird

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.droidsoftthird.databinding.FragmentScheduleHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //このActivityはHiltが使うと宣言し、依存関係をHiltから引っ張ってくる。
class ScheduleHomeFragment: Fragment(R.layout.fragment_schedule_home) {

    private val viewModel: ScheduleCalendarViewModel by activityViewModels() //TODO 生存期間流すぎるので後で短くするように
    private val binding: FragmentScheduleHomeBinding by dataBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val groupId = arguments?.getString("groupId")
        viewModel.setSelectedGroupId(groupId ?: "")
        setFragmentResult("key", bundleOf("result" to groupId))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            pager.apply {
                pager.adapter = ScheduleHomePagerAdapter(this@ScheduleHomeFragment)
                pager.isUserInputEnabled = false
            }
            /*tabLayout.let {
                TabLayoutMediator(it, pager) { tab, position -> tab.text = "OBJECT" + (position + 1) }.attach()
                it.getTabAt(0)?.setText(R.string.schedule_calendar) ?: throw IllegalStateException()
                it.getTabAt(1)?.setText(R.string.schedule_list) ?: throw IllegalStateException()
            }*/
            floatingActionButton.setOnClickListener {
                val action = ScheduleHomeFragmentDirections.actionScheduleHomeFragmentToScheduleCreateFragment()
                Navigation.findNavController(it).navigate(action)
            }
        }
    }
}
