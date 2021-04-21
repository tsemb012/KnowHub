package com.example.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.droidsoftthird.databinding.FragmentGroupDetailBinding
import com.google.android.material.appbar.CollapsingToolbarLayout


class GroupDetailFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentGroupDetailBinding = inflate(
            inflater, R.layout.fragment_group_detail, container, false
        )

        //-----ViewObjects for Navigation
        val layout: CollapsingToolbarLayout = binding.collapsingToolbarLayout
        val toolbar: Toolbar = binding.materialToolbar
        toolbar.title = " "//TODO ツールバータイトルの良い表示方法を検討する。

        //-----NavUI Objects
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController
        val appBarConfiguration = AppBarConfiguration.Builder(navController.getGraph()).build()

        //-----Setup for NavigationUI
        NavigationUI.setupWithNavController(
            layout,
            toolbar,
            navController,
            appBarConfiguration
        )

        val repository = GroupRepository()
        val arguments = GroupDetailFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = GroupDetailViewModelFactory(arguments.groupId, repository)
        val viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(GroupDetailViewModel::class.java)

        //DONE GroupDetailViewModelのコーディング
        //DONE GroupDetailViewModelFactoryのコーディング

        binding.groupDetailViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //TODO FloatingFabのOnClickロジックはこちらに記載する。

        return binding.root
    }

}
