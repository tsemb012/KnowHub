package com.example.droidsoftthird
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint//has to be on MainActivity
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val viewModel:MainViewModel by viewModels()
    private lateinit var navHeaderBinding: NavHeaderBinding;


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val bottomNav: BottomNavigationView = binding.bottomNav
        val drawer: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        navHeaderBinding = DataBindingUtil.inflate(layoutInflater,R.layout.nav_header,navView,false)
        navView.addHeaderView(navHeaderBinding.root)
  /*      ActionBarDrawerToggle(this,drawer,binding.toolbar,R.string.open_drawer, R.string.close_drawer)
            .drawerArrowDrawable.color = resources.getColor(R.color.primary_white)*/

        //Navigation関連
        //-----NavHost
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.myPageFragment,R.id.scheduleFragment,R.id.videoFragment,R.id.createProfileFragment),drawer)

        //下でNavControllerをより使いやすくするようにセットアップしている。セットアップしなければ、コントローラーを使っても動かない。
        //アクションバーもアップボタンなどで使うので、しっかりセットアップする。
        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.setupWithNavController(bottomNav, navController)
        setupActionBarWithNavController(navController,appBarConfiguration)//特定のフラグメントのみUpアイコンを表示させない。
        navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
            when(navDestination.id){//ここで全てのNavigationUIを管理している。
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
                    viewModel.authenticationState.observe(this, Observer { authenticationState ->//HomeFragmentにも同じロジックがあるが、役割が違う。
                        when (authenticationState) {
                            MainViewModel.AuthenticationState.AUTHENTICATED -> {
                                Timber.d("check flow")
                                viewModel.getUser()
                                viewModel.userProfile.observe(this, Observer { userProfile ->
                                    if (userProfile != null) {
                                        navHeaderBinding.viewModel = viewModel//ユーザーログインチェック　→　ユーザー情報取得＆Observe　→　ViewModelにLiveDataを入れて、Layoutに分散。
                                    }
                                })
                            }
                        }
                    })
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
        navView.setNavigationItemSelectedListener{
            if (it.itemId == R.id.log_out){
                viewModel.clearUserProfile()
                drawer.close()
                AuthUI.getInstance().signOut(this)

            } else {
                //WRITE CODE FOR MENU EXCEPT FOR SIGN_OUT
            }
            return@setNavigationItemSelectedListener true
        }
        binding.toolbar.inflateMenu(R.menu.menu_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }
}



/*TODO Animationを取り入れる際に再利用する。
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