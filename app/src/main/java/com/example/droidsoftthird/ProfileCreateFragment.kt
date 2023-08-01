package com.example.droidsoftthird

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileCreateFragment : ProfileSubmitFragment() {

    private val createViewModel: ProfileCreateViewModel by viewModels()
    override val viewModel by lazy { createViewModel }

    override fun setupSubmitListeners() = binding.submitProfileBtn.setOnClickListener {
        createViewModel.submitNewUserProfile()
    }

    override fun navigateTo() = findNavController().navigate(R.id.action_profileCreateFragment_to_homeFragment)
}