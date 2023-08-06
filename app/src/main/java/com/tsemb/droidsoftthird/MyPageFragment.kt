package com.tsemb.droidsoftthird

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tsemb.droidsoftthird.composable.group.screen.MyPageScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment: Fragment() {

    private val viewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
        viewModel.getMyGroups()
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MyPageScreen(viewModel, ::navigateToGroupDetail)
        }
    }

    private fun navigateToGroupDetail(groupId: String) {
        findNavController().navigate(
            MyPageFragmentDirections.actionMyPageFragmentToChatRoomFragment(groupId, "")
        )
    }
}
