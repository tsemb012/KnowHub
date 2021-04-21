package com.example.droidsoftthird

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.inflate
import androidx.lifecycle.ViewModelProvider
import com.example.droidsoftthird.databinding.FragmentGroupDetailBinding
import com.example.droidsoftthird.databinding.FragmentPagerRecommendBinding


class GroupDetailFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentGroupDetailBinding = inflate(
            inflater, R.layout.fragment_group_detail, container, false)
        val repository = GroupRepository()
        val arguments = GroupDetailFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = GroupDetailViewModelFactory(arguments.groupId,repository)
        val viewModel = ViewModelProvider(
            this, viewModelFactory).get(GroupDetailViewModel::class.java)

        //DONE GroupDetailViewModelのコーディング
        //DONE GroupDetailViewModelFactoryのコーディング

        binding.groupDetailViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //TODO FloatingFabのOnClickロジックはこちらに記載する。

        return binding.root
    }

}
