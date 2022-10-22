package com.example.droidsoftthird

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileCreateFragment : ProfileSubmitFragment() {

    private val createViewModel: ProfileCreateViewModel by viewModels()
    override val viewModel = createViewModel

    override fun setupSubmitListeners() = binding.submitProfileBtn.setOnClickListener { createViewModel.submitNewUserProfile() }

}