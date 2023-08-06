package com.tsemb.droidsoftthird

import android.os.Bundle
import android.view.*
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.droidsoftthird.R
import com.tsemb.droidsoftthird.composable.group.content.CommonAddButton
import com.example.droidsoftthird.databinding.FragmentScheduleHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class ScheduleHomeFragment: Fragment(R.layout.fragment_schedule_home) {

    private val viewModel: ScheduleCalendarViewModel by activityViewModels() //TODO 生存期間流すぎるので後で短くするように
    private val binding: FragmentScheduleHomeBinding by dataBinding()
    private val bottomNavigationView: BottomNavigationView? by lazy { requireActivity().findViewById(
        R.id.bottom_nav
    ) }
    private var isNavigatedFromChatGroup by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val groupId = arguments?.getString("groupId")
        isNavigatedFromChatGroup = groupId != null
        viewModel.setSelectedGroupId(groupId ?: "")
        viewModel.setIsNavigatedFromChatGroup(isNavigatedFromChatGroup)
        setFragmentResult("key", bundleOf("result" to groupId))
        if(isNavigatedFromChatGroup) bottomNavigationView?.isGone = true
    }

    override fun onResume() {
        super.onResume()
        if(isNavigatedFromChatGroup) bottomNavigationView?.isGone = true
        if(viewModel.uiModel.value?.isGroupFixed == true) bottomNavigationView?.isGone = true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            pager.apply {
                pager.adapter = ScheduleHomePagerAdapter(this@ScheduleHomeFragment)
                pager.isUserInputEnabled = false
            }

            floatingActionButtonCompose.setContent {
                CommonAddButton(
                    label = "イベントを追加",
                    navigate = {
                        findNavController().navigate(
                            ScheduleHomeFragmentDirections.actionScheduleHomeFragmentToScheduleCreateFragment(
                                isNavigatedFromChatGroup,
                                viewModel.uiModel.value?.selectedSimpleGroup?.groupId ?: ""
                            )
                        ) },
                    modifier = Modifier.padding(bottom = 32.dp, end = 16.dp)
                )
            }
        }
    }
}
