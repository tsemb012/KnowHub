package com.example.droidsoftthird
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.droidsoftthird.databinding.ActivityMainBinding
import com.example.droidsoftthird.databinding.NavHeaderBinding
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint//has to be on MainActivity
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val viewModel:MainViewModel by viewModels()
    private val binding:ActivityMainBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_main) }
    private lateinit var navHeaderBinding: NavHeaderBinding;
    


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navHeaderBinding = DataBindingUtil.inflate(layoutInflater,R.layout.nav_header,binding.navView,false)
        binding.navView.addHeaderView(navHeaderBinding.root)


        //-----NavHost
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.myPageFragment,R.id.ScheduleHomeFragment,R.id.videoFragment,R.id.createProfileFragment),binding.drawerLayout)

        NavigationUI.setupWithNavController(binding.navView, navController)
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
        setupActionBarWithNavController(navController,appBarConfiguration)//特定のフラグメントのみUpアイコンを表示させない。
        navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
            when(navDestination.id){
                R.id.homeFragment,
                R.id.signUpFragment,
                R.id.signInFragment -> {
                    binding.bottomNav.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE
                }
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
                    viewModel.authenticationState.removeObservers(this)
                    viewModel.authenticationState.observe(this, Observer { authenticationState ->
                        when (authenticationState) {
                            MainViewModel.AuthenticationState.AUTHENTICATED -> {
                                viewModel.getUser()
                                viewModel.userProfile.observe(this, Observer { userProfile ->
                                    if (userProfile != null) {
                                        navHeaderBinding.viewModel = viewModel
                                    }
                                })
                            }
                            MainViewModel.AuthenticationState.UNAUTHENTICATED -> {  }
                        }
                    })
                }
                R.id.myPageFragment -> {
                    binding.toolbar.title = getString(R.string.my_page)
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbar.navigationIcon = null
                }
                R.id.ScheduleHomeFragment -> {
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
        binding.navView.setNavigationItemSelectedListener{
            if (it.itemId == R.id.log_out){
                viewModel.clearUserProfile()
                binding.drawerLayout.close()
                AuthUI.getInstance().signOut(this)

            } else {
                //WRITE CODE FOR MENU EXCEPT FOR SIGN_OUT
            }
            return@setNavigationItemSelectedListener true
        }
        binding.toolbar.inflateMenu(R.menu.home)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        toolbar?.inflateMenu(R.menu.home)
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }
}
