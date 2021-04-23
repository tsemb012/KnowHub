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
import timber.log.Timber


class GroupDetailFragment : Fragment() {

    //TODO UIを洗練させる
    //TODO 同フラグメント専用にBindingUtilのコードを作る。
    //TODO ツールバータイトルの良い表示方法を検討する。
    //TODO getGroup()の記述位置があっているか確認する。
    //TODO FloatingFabのOnClickロジック及び設計を考える。

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
        toolbar.title = " "

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

        val repository = UserGroupRepository()
        val arguments = GroupDetailFragmentArgs.fromBundle(requireArguments())
        Timber.tag(TAG).d(arguments.toString())
        val viewModelFactory = GroupDetailViewModelFactory(arguments.groupId, repository)
        val viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(GroupDetailViewModel::class.java)

        //DONE GroupDetailViewModelのコーディング
        //DONE GroupDetailViewModelFactoryのコーディング

        binding.groupDetailViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getGroup()





        return binding.root
    }

    companion object {
        private val TAG: String? = GroupDetailFragment::class.simpleName
    }
}
