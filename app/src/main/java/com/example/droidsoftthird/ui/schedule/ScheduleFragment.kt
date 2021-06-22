package com.example.droidsoftthird.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.droidsoftthird.R
import com.example.droidsoftthird.ScheduleViewPagerAdapter
import com.example.droidsoftthird.databinding.FragmentScheduleBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding;
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //-----Enable Menu
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //-----ViewObjects for Navigation
        val layout: CollapsingToolbarLayout = binding.include.collapsingToolbarLayout
        val toolbar: Toolbar = binding.include.toolbar
        val bottomNav: BottomNavigationView = binding.bottomNav

        //-----NavUI Objects
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.homeFragment,
            R.id.myPageFragment,
            R.id.scheduleFragment,
            R.id.videoFragment)).build()


        //-----Setup for NavigationUI
        NavigationUI.setupWithNavController(layout, toolbar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(bottomNav, navController)


        //-----ViewPager Objects
        val scheduleViewPagerAdapter = ScheduleViewPagerAdapter(this)
        val viewPager = binding.pager
        viewPager.offscreenPageLimit = 1 //TODO ライフサイクルの挙動が変化しているか確認する。
        viewPager.adapter = scheduleViewPagerAdapter

        //-----TabLayout&ViewPager Linking
        val tabLayout: TabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager
        ) { tab: TabLayout.Tab, position: Int -> tab.text = "OBJECT" + (position + 1) }.attach()

        //-----SettingTab *After Linking Tab&ViewPager*
        requireNotNull(tabLayout.getTabAt(0)).setText(R.string.approved)
        requireNotNull(tabLayout.getTabAt(1)).setText(R.string.pending)

        //-----Navigation to AddGroupFragment by FloatingActionButton
        //TODO 関心の分離のため、Eventクラスを用いてクリックイベントをViewModelに移行する。

        binding.floatingActionButton.setOnClickListener(View.OnClickListener { v ->

        /*TODO Detail作成時にFloatingの画面遷移を処理する。

            val action: NavDirections =
                HomeFragmentDirections.actionHomeFragmentToAddGroupFragment()
            Navigation.findNavController(v).navigate(action)
        */
        })
    }


    companion object {

    }
}
