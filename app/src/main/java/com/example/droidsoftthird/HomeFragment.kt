package com.example.droidsoftthird

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.droidsoftthird.databinding.FragmentHomeBinding
import com.example.droidsoftthird.ui.entrance.Screen
import com.example.droidsoftthird.ui.entrance.navigate
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        if (FirebaseAuth.getInstance().currentUser == null) {
            backToWelcome()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // ViewPager Objects
        val homeViewPagerAdapter = HomeViewPagerAdapter(this)
        binding.pager.apply {
            adapter = homeViewPagerAdapter
            isUserInputEnabled = false
        }

        // TabLayout & ViewPager Linking
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position -> tab.text = "OBJECT${position + 1}" }.attach()

        // Setting Tab
        binding.tabLayout.getTabAt(0)?.setText(R.string.recommendation)
        binding.tabLayout.getTabAt(1)?.setText(R.string.map)

        // Navigation to AddGroupFragment by FloatingActionButton
        binding.floatingActionButton.setOnClickListener { v ->
            val action = HomeFragmentDirections.actionHomeFragmentToAddGroupFragment()
            navController.navigate(action)
        }
    }

    private fun backToWelcome() {
        navigate(Screen.Welcome, Screen.Home)
    }
}
