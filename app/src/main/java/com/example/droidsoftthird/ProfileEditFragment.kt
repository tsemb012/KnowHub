package com.example.droidsoftthird

import android.os.Bundle
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment :ProfileSubmitFragment() {

    private val editViewModel: ProfileEditViewModel by viewModels()
    override val viewModel: ProfileSubmitViewModel by lazy { editViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editViewModel.fetchUserDetail()
    }

    override fun setupSubmitListeners() = binding.submitProfileBtn.setOnClickListener { editViewModel.submitEditedUserProfile() }
}
