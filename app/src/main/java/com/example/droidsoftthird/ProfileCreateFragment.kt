package com.example.droidsoftthird

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileCreateFragment : ProfileSubmitFragment() {

    private val createViewModel = viewModel as ProfileCreateViewModel
    override fun setupSubmitListeners() = binding.submitProfileBtn.setOnClickListener { createViewModel.submitNewUserProfile() }

}