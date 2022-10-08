package com.example.droidsoftthird

import androidx.fragment.app.Fragment
import com.example.droidsoftthird.databinding.FragmentProfileBinding
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment:Fragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by dataBinding()

}