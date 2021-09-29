package com.example.droidsoftthird

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.compose.navigate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.example.droidsoftthird.databinding.FragmentMyPageBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MyPageFragment: Fragment() {

    private lateinit var binding: FragmentMyPageBinding
    private lateinit var appBarConfiguration: AppBarConfiguration//不要
    private lateinit var navController: NavController
    private val viewModel:MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true);
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //-----ViewObjects for Navigation
        /*val toolbar: Toolbar = binding.toolbar
        toolbar.setTitle(R.string.my_page)*/

        requireActivity().actionBar?.title = getString(R.string.my_page)//TODO MainActivityと重複してないか？削除しても問題ないのでは？

        //-----NavUI Objects
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        /*appBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.homeFragment,R.id.myPageFragment,R.id.scheduleFragment,R.id.videoFragment)).build()

        //-----Setup for NavigationUI
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)

        //-----MenuGenerate for AppBar
        toolbar.inflateMenu(R.menu.menu_main)*/ //TODO ここでは良いが、実際のコードでは不要な部分削除しよう。

        /*TODO FilterをMenuに付与する際に再利用するコード
        binding.include.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.sign_out) {
                context?.let { AuthUI.getInstance().signOut(it) }
                startSignIn()
            } else {
                //TODO WRITE CODE FOR MENU EXCEPT FOR SIGN_OUT
            }
            return@setOnMenuItemClickListener NavigationUI.onNavDestinationSelected(item,
                navController)
                    || super.onOptionsItemSelected(item)
        }*/

        val adapter = GroupAdapter(GroupListener{ groupId, groupName ->
            viewModel.onGroupClicked(groupId, groupName)
        })
        binding.groupList.adapter = adapter

        viewModel.getMyGroups()

        viewModel.groups.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.let {
                adapter.submitList(it) }
        })

        viewModel.navigateToChatRoom.observe(viewLifecycleOwner, androidx.lifecycle.Observer{ groupInfo ->
            groupInfo?.let {

                val action =
                    MyPageFragmentDirections.actionMyPageFragmentToChatRoomFragment(it.first,it.second)
                navController.navigate(action)
                viewModel.onChatRoomNavigated()
            }
        })//DONE Navigationを設定し、上記にコードを書き換える。

        val manager = GridLayoutManager(activity,2, GridLayoutManager.VERTICAL,false)

        binding.groupList.layoutManager = manager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }//TODO 使わないところは消しましょう。

    /*//TODO Filter製作時に再利用する。
    *//**
     * Inflates the overflow menu that contains filtering options.
     *//*
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    *//**
     * Updates the filter in the [OverviewViewModel] when the menu items are selected from the
     * overflow menu.
     *//*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_rent_menu -> MarsApiFilter.SHOW_RENT
                R.id.show_buy_menu -> MarsApiFilter.SHOW_BUY
                else -> MarsApiFilter.SHOW_ALL
            }
        )
        return true
    }*/
}
