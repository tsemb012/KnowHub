package com.example.droidsoftthird
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.example.droidsoftthird.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val viewModel:MainViewModel by viewModels()
    private val binding:ActivityMainBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.myPageFragment,R.id.ScheduleHomeFragment,R.id.profileCreateFragment, R.id.profileFragment))

        NavigationUI.setupWithNavController(binding.bottomNav, navController)
        navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
            when(navDestination.id){
                R.id.welcomeFragment,
                R.id.signUpFragment,
                R.id.signInFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.profileCreateFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.homeFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    viewModel.authenticationState.removeObservers(this)//TODO ここも削除
                    viewModel.authenticationState.observe(this, Observer { authenticationState ->
                        when (authenticationState) {
                            MainViewModel.AuthenticationState.AUTHENTICATED -> {
                                viewModel.getUser()
                                viewModel.userProfile.observe(this, Observer { userProfile ->
                                    if (userProfile != null) {
                                    }
                                })
                            }
                            MainViewModel.AuthenticationState.UNAUTHENTICATED -> {  }
                            else -> {}
                        }
                    })
                }
                R.id.myPageFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
                R.id.ScheduleHomeFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
                R.id.addGroupFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.groupDetailFragment-> {
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.chatRoomFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                R.id.profileFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
                else -> " "
            }
        }

        viewModel.loginState.observe(this, Observer { loginState ->
            when (loginState) {
                MainViewModel.LoginState.LOGGED_IN -> { }
                MainViewModel.LoginState.LOGGED_OUT -> {
                    clearCache()
                    signOut()
                }
                else -> {}
            }
        })
    }

    private fun clearCache() {
        viewModel.also {
            it.clearUserProfile()
            it.clearTokenCache()
        }
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(this)
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()
    }
}
