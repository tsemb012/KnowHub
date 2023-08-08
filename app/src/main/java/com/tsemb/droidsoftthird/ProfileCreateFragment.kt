package com.tsemb.droidsoftthird

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tsemb.droidsoftthird.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileCreateFragment : ProfileSubmitFragment() {

    private val createViewModel: ProfileCreateViewModel by viewModels()
    override val viewModel by lazy { createViewModel }

    override fun setupSubmitListeners() = binding.submitProfileBtn.setOnClickListener {
        createViewModel.submitNewUserProfile()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            Toast.makeText(context, "プロフィールを入力してください", Toast.LENGTH_SHORT).show()
        }
    }

    override fun navigateTo() = findNavController().navigate(R.id.action_profileCreateFragment_to_homeFragment)
}