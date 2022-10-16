package com.example.droidsoftthird

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.droidsoftthird.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment: Fragment() {

    private lateinit var binding: FragmentMyPageBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val viewModel:MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true);
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        requireActivity().actionBar?.title = getString(R.string.my_page)

        //-----NavUI Objects
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val adapter = GroupAdapter(GroupListener{ groupId, groupName ->
            viewModel.onGroupClicked(groupId, groupName)
        })
        binding.groupList.adapter = adapter

        viewModel.getMyGroups()

        viewModel.groups.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }

        viewModel.message.observe(viewLifecycleOwner) {
            it.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.navigateToChatRoom.observe(viewLifecycleOwner, androidx.lifecycle.Observer{ groupInfo ->
            groupInfo?.let {

                val action =
                    MyPageFragmentDirections.actionMyPageFragmentToChatRoomFragment(it.first,it.second)
                navController.navigate(action)
                viewModel.onChatRoomNavigated()
            }
        })//DONE Navigationを設定し、上記にコードを書き換える。

        val manager = GridLayoutManager(activity,2, GridLayoutManager.VERTICAL,false)

        binding.groupList.layoutManager = manager

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
