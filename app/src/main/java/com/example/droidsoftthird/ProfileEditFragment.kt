package com.example.droidsoftthird

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment :ProfileSubmitFragment() {

    private val editViewModel = viewModel as ProfileEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editViewModel.fetchUserDetail()
    }

    override fun setupSubmitListeners() = binding.submitProfileBtn.setOnClickListener { editViewModel.submitEditedUserProfile() }
}
