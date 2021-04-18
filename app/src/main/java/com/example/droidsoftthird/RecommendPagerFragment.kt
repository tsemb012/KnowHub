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
import com.example.droidsoftthird.databinding.FragmentPagerRecommendBinding

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



        val adapter = GroupAdapter(GroupListener{ groupId ->
            recommendPagerViewModel.onGroupClicked(groupId)
        })//GridItemがクリックされた瞬間に、MutableLiveDataにIDを渡す。


        binding.groupList.adapter = adapter

        recommendPagerViewModel.groups.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        recommendPagerViewModel.navigateToGroupDetail.observe(viewLifecycleOwner, Observer { groupId ->
            groupId?.let {
                this.findNavController().navigate(
                    RecommendPagerFragmentDirections.actionRecommendPagerFragmentToGroupDetailFragment(groupId)
                )//MutableLiveDataにIDが渡された瞬間に、画面遷移を実行する。
                recommendPagerViewModel.onGroupDetailNavigated()
            }
        })












        //return super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

}