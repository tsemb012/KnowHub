package com.example.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.droidsoftthird.databinding.FragmentPagerRecommendBinding
import com.google.android.material.snackbar.Snackbar

class RecommendPagerFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPagerRecommendBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pager_recommend, container, false)
        val repository = GroupRepository()
        val viewModelFactory = RecommendPagerViewModelFactory(repository)
        val recommendPagerViewModel = ViewModelProvider(
                this, viewModelFactory).get(RecommendPagerViewModel::class.java)

        binding.recommendPagerViewModel = recommendPagerViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = GroupAdapter(GroupListener{ groupId ->
            recommendPagerViewModel.onGroupClicked(groupId)
        })//GridItemがクリックされた瞬間に、MutableLiveDataにIDを渡す。
        binding.groupList.adapter = adapter

        recommendPagerViewModel.getAllGroups()
        //TODO 上記、初期化メソッドの適切な位置を検討する。

        recommendPagerViewModel.groups.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        recommendPagerViewModel.navigateToGroupDetail.observe(viewLifecycleOwner, Observer { groupId ->
            groupId?.let {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToGroupDetailFragment(groupId)
                )
                recommendPagerViewModel.onGroupDetailNavigated()
            }
        })



        val manager = GridLayoutManager(activity,2, GridLayoutManager.VERTICAL,false)

        binding.groupList.layoutManager = manager

        return binding.root
    }

}