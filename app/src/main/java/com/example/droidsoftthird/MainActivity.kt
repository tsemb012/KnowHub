package com.example.droidsoftthird

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.droidsoftthird.databinding.ActivityMainBinding
import com.example.droidsoftthird.databinding.FragmentHomeBinding
import com.example.droidsoftthird.databinding.NavHeaderBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint//has to be on MainActivity
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val viewModel:MainViewModel by viewModels()
    lateinit var binding_header: NavHeaderBinding;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding_header = DataBindingUtil.inflate(layoutInflater,R.layout.nav_header,binding.navView,false)
        binding.navView.addHeaderView(binding_header.root)


        val bottomNav: BottomNavigationView = binding.bottomNav
        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        val drawer: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

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
        //appBarConfiguration = AppBarConfiguration(navController.graph)


        //NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.setupWithNavController(bottomNav, navController)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.myPageFragment,R.id.scheduleFragment,R.id.videoFragment,R.id.createProfileFragment),drawer)
        setupActionBarWithNavController(navController,appBarConfiguration)//特定のフラグメントのみUpアイコンを表示させない。

        binding.navView.setNavigationItemSelectedListener{
            if (it.itemId == R.id.log_out){
                viewModel.clearUserProfile()
                AuthUI.getInstance().signOut(this)

            } else {
                //TODO WRITE CODE FOR MENU EXCEPT FOR SIGN_OUT
            }
            return@setNavigationItemSelectedListener true
        }
        binding.toolbar.inflateMenu(R.menu.menu_main)

        navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->

            when(navDestination.id){

                R.id.createProfileFragment -> {
                    binding.toolbar.title = getString(R.string.input_profile)
                    binding.bottomNav.visibility = View.GONE
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbar.navigationIcon = null

                }

                R.id.homeFragment -> {
                    binding.toolbar.title = getString(R.string.search)
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                }
                R.id.myPageFragment -> {
                    binding.toolbar.title = getString(R.string.my_page)
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbar.navigationIcon = null
                }
                R.id.scheduleFragment -> {
                    binding.toolbar.title = getString(R.string.schedule)
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbar.navigationIcon = null
                }
                R.id.videoFragment -> {
                    binding.toolbar.title = getString(R.string.video)
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbar.navigationIcon = null
                }
                R.id.addGroupFragment -> {
                    binding.toolbar.title = getString(R.string.group_add)
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.groupDetailFragment-> {
                    binding.bottomNav.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE
                }
                R.id.chatRoomFragment -> {
                    binding.bottomNav.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE
                }
                else -> " "
            }
        }

        viewModel.authenticationState.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                MainViewModel.AuthenticationState.AUTHENTICATED -> {
                    viewModel.getUser()//画面への反映および画面遷移ついては、ViewModel主導で行う。
                    viewModel.userProfile.observe(this, Observer { userProfile ->
                        if (userProfile != null) {
                            binding_header.viewModel = viewModel
                        } else {
                            //navController.navigate(R.id.createProfileFragment)
                            //DONE CreateProfileFragmentを作成する。
                        }
                    })
                }
                else -> {
                    //binding_header.viewModel = null
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }
}