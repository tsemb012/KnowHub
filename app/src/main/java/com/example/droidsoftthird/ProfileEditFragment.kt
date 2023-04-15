package com.example.droidsoftthird

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.model.presentation_model.LoadState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment :ProfileSubmitFragment() {

    private val editViewModel: ProfileEditViewModel by viewModels()
    override val viewModel: ProfileSubmitViewModel by lazy { editViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editViewModel.fetchUserDetail()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editViewModel.uiModel.observe(viewLifecycleOwner) { uiModel ->
            when (uiModel.loadState) {
                is LoadState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is LoadState.Loaded<*> -> binding.progressBar.visibility = View.GONE
                is LoadState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.submitProfileBtn.isEnabled = false
                    Toast.makeText(requireContext(), "Error: ${uiModel.loadState.error}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    override fun setupSubmitListeners() = binding.submitProfileBtn.setOnClickListener { editViewModel.submitEditedUserProfile() }
}
