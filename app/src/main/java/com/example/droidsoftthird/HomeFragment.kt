package com.example.droidsoftthird

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        if (FirebaseAuth.getInstance().currentUser == null) {
            backToWelcome()
        } else {
            activityViewModel.checkLoginState()
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
        setupViewPager()
    }

    private fun setupViewPager() {
        binding.pager.apply {
            adapter =  HomeViewPagerAdapter(this@HomeFragment)
            isUserInputEnabled = false
        }

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = "OBJECT${position + 1}"
        }.attach()

        binding.tabLayout.getTabAt(0)?.setText(R.string.newest_first_order)
        binding.tabLayout.getTabAt(1)?.setText(R.string.map)
    }

    private fun backToWelcome() {
        navigate(Screen.Welcome, Screen.Home)
    }
}
