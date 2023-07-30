package com.example.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.droidsoftthird.composable.profile.ProfileScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment:Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()
    private val navController by lazy { NavHostFragment.findNavController(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    ProfileScreen(
                        viewModel = viewModel,
                        toProfileEdit = { navController.navigate(R.id.profileEditFragment) },
                        toGroupDetail = { groupId -> navController.navigate(ProfileFragmentDirections.actionProfileFragmentToGroupDetailFragment(groupId)) },
                        toEventDetail = { eventId -> navController.navigate(ProfileFragmentDirections.actionProfileFragmentToScheduleDetailFragment(eventId)) },
                    ) { activityViewModel.logout() }
                }
            }
        }
    }
}