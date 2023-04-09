package com.example.droidsoftthird

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.droidsoftthird.databinding.FragmentHomeBinding
import com.example.droidsoftthird.ui.entrance.Screen
import com.example.droidsoftthird.ui.entrance.navigate
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding;
    private lateinit var navController: NavController
    private val viewModel:HomeViewModel by viewModels()

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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        //-----ViewPager Objects
        val homeViewPagerAdapter = HomeViewPagerAdapter(this)
        val viewPager = binding.pager
        viewPager.adapter = homeViewPagerAdapter
        viewPager.isUserInputEnabled = false

        //-----TabLayout&ViewPager Linking
        val tabLayout: TabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager
        ) { tab: TabLayout.Tab, position: Int -> tab.text = "OBJECT" + (position + 1) }.attach()

        //-----SettingTab *After Linking Tab&ViewPager*
        requireNotNull(tabLayout.getTabAt(0)).setText(R.string.recommendation)
        requireNotNull(tabLayout.getTabAt(1)).setText(R.string.map)

        //-----Navigation to AddGroupFragment by FloatingActionButton

        binding.floatingActionButton.setOnClickListener(View.OnClickListener { v ->
            val action: NavDirections =
                HomeFragmentDirections.actionHomeFragmentToAddGroupFragment()
            Navigation.findNavController(v).navigate(action)
        })//TODO 関心の分離のため、Eventクラスを用いてクリックイベントをViewModelに移行する。

        observeAuthenticationState()
    }

    private fun observeAuthenticationState() {

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                HomeViewModel.AuthenticationState.AUTHENTICATED -> {
                    viewModel.getUser()
                    viewModel.userProfile.observe(viewLifecycleOwner, Observer { userProfile ->
                        if (userProfile == null) {
                            //TODO ユーザープロフィールの処理についてどうするか検討する。
                            //TODO データクラスごとひとまとめにして、nullを作らない方針の方がよくない？
                            //AuthUI.getInstance().signOut(requireActivity())
                            //navigate(Screen.Welcome, Screen.Home)
                            //navController.navigate(R.id.createProfileFragment)
                        }
                    })
                }
                HomeViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    backToWelcome()
                }
            }
        })
    }

    private fun backToWelcome() { navigate(Screen.Welcome, Screen.Home) }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home,menu)
        val primaryWhite = "#F6FFFE"
        menu.getItem(0).icon?.apply {
            mutate() // Drawableを変更可能にする
            setColorFilter(android.graphics.Color.parseColor(primaryWhite), PorterDuff.Mode.SRC_ATOP) // アイコンを白くする
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.filter -> {
                TODO("Filterの機能を追加する。")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
