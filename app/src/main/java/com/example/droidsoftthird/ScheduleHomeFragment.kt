package com.example.droidsoftthird

import android.graphics.PorterDuff
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.droidsoftthird.databinding.ActivityMainBinding
import com.example.droidsoftthird.databinding.FragmentScheduleHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //このActivityはHiltが使うと宣言し、依存関係をHiltから引っ張ってくる。
class ScheduleHomeFragment: Fragment() {

    private val binding: FragmentScheduleHomeBinding by dataBinding()
    private val viewModel: ScheduleHomeViewModel by viewModels()
    private val navController: NavController by lazy {
        (activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            pager.adapter = ScheduleHomePagerAdapter(this@ScheduleHomeFragment)
            tabLayout.let {
                TabLayoutMediator(it, pager) {
                    tab, position -> tab.text = "OBJECT" + (position + 1)
                }.attach()
                it.getTabAt(0)?.setText(R.string.schedule_registered) ?: throw IllegalStateException()
                it.getTabAt(1)?.setText(R.string.schedule_proposed) ?: throw IllegalStateException()
            }
            floatingActionButton.setOnClickListener {
                val action = ScheduleHomeFragmentDirections.actionScheduleHomeFragmentToScheduleCreateFragment()
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home,menu)
        val primaryWhite = "#F6FFFE"
        menu.getItem(0).icon.apply {
            mutate() // Drawableを変更可能にする
            setColorFilter(android.graphics.Color.parseColor(primaryWhite), PorterDuff.Mode.SRC_ATOP) // アイコンを白くする
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.filter -> {
                TODO("Filterの機能を追加する。")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}