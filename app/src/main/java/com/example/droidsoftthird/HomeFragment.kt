package com.example.droidsoftthird

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.droidsoftthird.databinding.FragmentHomeBinding
import com.example.droidsoftthird.databinding.NavHeaderBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding;
    private lateinit var binding_header: NavHeaderBinding;
    private lateinit var appBarConfiguration: AppBarConfiguration
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
        binding_header = DataBindingUtil.inflate(layoutInflater,R.layout.nav_header,binding.navView,false)
        binding.navView.addHeaderView(binding_header.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //-----ViewObjects for Navigation
        val layout: CollapsingToolbarLayout = binding.include.collapsingToolbarLayout
        val toolbar: Toolbar = binding.include.toolbar
        val drawer: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        //-----NavUI Objects
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.homeFragment,R.id.myPageFragment,R.id.scheduleFragment,R.id.videoFragment)).setOpenableLayout(
            drawer).build()//TODO TopLevelDestinationが増えるごとに追加していく。


        //-----Setup for NavigationUI
        NavigationUI.setupWithNavController(layout, toolbar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)


        //-----MenuGenerate for AppBar
        binding.include.toolbar.inflateMenu(R.menu.menu_main)
        /**TODO FilterをMenuに付与する際に再利用するコード
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
        }*/
        binding.navView.setNavigationItemSelectedListener{
            if (it.itemId == R.id.log_out){
                context?.let { AuthUI.getInstance().signOut(it) }
        } else {
            //TODO WRITE CODE FOR MENU EXCEPT FOR SIGN_OUT
        }
            return@setNavigationItemSelectedListener true
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
        requireNotNull(tabLayout.getTabAt(1)).setText(R.string.map)

        //-----Navigation to AddGroupFragment by FloatingActionButton
        //TODO 関心の分離のため、Eventクラスを用いてクリックイベントをViewModelに移行する。
        binding.floatingActionButton.setOnClickListener(View.OnClickListener { v ->
            val action: NavDirections =
                HomeFragmentDirections.actionHomeFragmentToAddGroupFragment()
            Navigation.findNavController(v).navigate(action)
        })

        observeAuthenticationState()
    }

    private fun observeAuthenticationState() {

        //DONE CreateProfileFragmentを作成する。
        //DONE UserProfileModelを作成する。
        //DONE HomeFragmentLayout内の処理を完成させる。
        //TODO　is Result.Error -> 取得失敗時のエラー記入

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                HomeViewModel.AuthenticationState.AUTHENTICATED -> {
                    viewModel.getUser()//画面への反映および画面遷移ついては、ViewModel主導で行う。
                    viewModel.userProfile.observe(viewLifecycleOwner, Observer { userProfile ->
                        if (userProfile != null) {
                            binding_header.viewModel = viewModel
                        } else {
                            navController.navigate(R.id.createProfileFragment)
                            //DONE CreateProfileFragmentを作成する。
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



    /**TODO Filter製作時に再利用する。
     *
     * Inflates the overflow menu that contains filtering options.

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

     * Updates the filter in the [OverviewViewModel] when the menu items are selected from the
     * overflow menu.

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_rent_menu -> MarsApiFilter.SHOW_RENT
                R.id.show_buy_menu -> MarsApiFilter.SHOW_BUY
                else -> MarsApiFilter.SHOW_ALL
            }
        )
        return true
    }*/


    companion object {
        const val RC_RESIGN_IN = 9002
    }
}
