package com.example.droidsoftthird

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.droidsoftthird.databinding.FragmentHomeBinding
import com.example.droidsoftthird.databinding.NavHeaderBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
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
                            navController.navigate(R.id.createProfileFragment)
                        }
                    })
                }
                else -> {
                    startSignIn()
                }
            }
        })
    }

    private fun startSignIn() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                //.setTheme(R.style.AppTheme_LogIn)
                //.setLogo(R.drawable.ic_baseline_school_24)
                .setAvailableProviders(providers)
                .build(),
            RC_RESIGN_IN )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==  RC_RESIGN_IN ) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser

                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // TODO Error&Success Handling after onActivityResult
            }
        }
    }

    companion object {
        const val RC_RESIGN_IN = 9002
    }
}
