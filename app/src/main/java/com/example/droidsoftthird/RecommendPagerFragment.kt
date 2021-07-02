package com.example.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.droidsoftthird.databinding.FragmentPagerRecommendBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.concurrent.timer

@AndroidEntryPoint//Enable this class to receive dependency from Hilt
class RecommendPagerFragment:Fragment() {

    private val viewModel:RecommendPagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentPagerRecommendBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pager_recommend, container, false)


        binding.recommendPagerViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = GroupAdapter(GroupListener{ groupId,_ ->
            viewModel.onGroupClicked(groupId)
        })//GridItemがクリックされた瞬間に、MutableLiveDataにIDを渡す。
        binding.groupList.adapter = adapter

        viewModel.getAllGroups()
        //TODO 上記、初期化メソッドの適切な位置を検討する。

        viewModel.groups.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        viewModel.navigateToGroupDetail.observe(viewLifecycleOwner, Observer { groupId ->
            groupId?.let {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToGroupDetailFragment(groupId)
                )
                viewModel.onGroupDetailNavigated()
            }
        })



        val manager = GridLayoutManager(activity,2, GridLayoutManager.VERTICAL,false)

        binding.groupList.layoutManager = manager

        return binding.root
    }

}