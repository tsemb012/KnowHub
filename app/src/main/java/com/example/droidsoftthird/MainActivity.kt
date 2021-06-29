package com.example.droidsoftthird

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.droidsoftthird.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint//has to be on MainActivity
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)
        setContentView(binding.root)

        val bottomNav: BottomNavigationView = binding.bottomNav

        /*TODO Navigationを取り入れる際に再利用する。
            val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setExitAnim(R.anim.nav_default_exit_anim)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_default_exit_anim)
            .build()
            bottomNav.setOnNavigationItemSelectedListener { item->
            when(item.itemId){
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment,null,options)
                }
                R.id.myPageFragment -> {
                    navController.navigate(R.id.myPageFragment,null,options)
                }
                R.id.scheduleFragment -> {
                    navController.navigate(R.id.scheduleFragment,null,options)
                }
                R.id.videoFragment -> {
                    navController.navigate(R.id.videoFragment,null,options)
                }
            }
            true
            }*/

        //-----NavHost
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
            if (navDestination.id == R.id.homeFragment
                || navDestination.id == R.id.myPageFragment
                || navDestination.id == R.id.scheduleFragment
                || navDestination.id == R.id.videoFragment) {
                binding.bottomNav.visibility = View.VISIBLE
            } else {
                binding.bottomNav.visibility = View.GONE
            }
        }

        appBarConfiguration = AppBarConfiguration(navController.graph)

        NavigationUI.setupWithNavController(bottomNav, navController)

    }




    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp()
    }




    companion object {
        const val RC_SIGN_IN = 9001
    }
}