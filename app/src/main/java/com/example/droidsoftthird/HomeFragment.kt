package com.example.droidsoftthird

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.droidsoftthird.databinding.FragmentHomeBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding;
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //-----Enable Menu
        setHasOptionsMenu(true);

        //-----ViewModel
        //TODO ViewModelを生成する。
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //-----ViewObjects for Navigation
        val layout: CollapsingToolbarLayout = binding.include.collapsingToolbarLayout
        val toolbar: Toolbar = binding.include.toolbar
        val drawer: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val bottomNav: BottomNavigationView = binding.bottomNav

        //-----NavUI Objects
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).setOpenableLayout(
            drawer).build()


        //-----Setup for NavigationUI
        NavigationUI.setupWithNavController(layout, toolbar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.setupWithNavController(bottomNav, navController)


        //-----MenuGenerate for AppBar
        binding.include.toolbar.inflateMenu(R.menu.menu_main)
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
        }

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
        requireNotNull(tabLayout.getTabAt(1)).setText(R.string.schedule)
        requireNotNull(tabLayout.getTabAt(2)).setText(R.string.map)

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
            RC_RESIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_RESIGN_IN) {
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
