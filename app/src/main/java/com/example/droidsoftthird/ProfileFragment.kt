package com.example.droidsoftthird

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.droidsoftthird.databinding.FragmentProfileBinding
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment:Fragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by dataBinding()
    private val viewModel:ProfileViewModel by viewModels()
    private val navController by lazy { NavHostFragment.findNavController(this) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        setupNavAppBar()
        setupClickListeners()
        viewModel.fetchUserDetail()
    }

    private fun setupNavAppBar() {
        //val navController = (requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        //TODO 上記のnavControllerで遷移できない場合は、あらためて検討する。
        val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        NavigationUI.setupWithNavController(
                binding.collapsingToolbarLayout,
                binding.materialToolbar.apply { title = " " },
                navController,
                appBarConfiguration
        )
    }

    private fun setupClickListeners() =
        binding.transitToEditProfileFab.setOnClickListener {
            navController.navigate(R.id.profileEditFragment)
        }
}