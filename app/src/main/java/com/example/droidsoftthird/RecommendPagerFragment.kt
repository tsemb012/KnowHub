package com.example.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.droidsoftthird.databinding.FragmentPagerRecommendBinding
import com.example.droidsoftthird.utils.EndlessScrollListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint//Enable this class to receive dependency from Hilt
class RecommendPagerFragment:Fragment() {

    private val viewModel:RecommendPagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPagerRecommendBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pager_recommend, container, false)

        binding.recommendPagerViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = GroupAdapter(GroupListener{ groupId,_ ->
            viewModel.onGroupClicked(groupId)
        })//GridItemがクリックされた瞬間に、MutableLiveDataにIDを渡す。
        binding.groupList.adapter = adapter

        viewModel.initialize() //TODO　初期ページを表示する。

        viewModel.groups.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.navigateToGroupDetail.observe(viewLifecycleOwner) { groupId ->
            groupId?.let {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToGroupDetailFragment(groupId)
                )
                viewModel.onGroupDetailNavigated()
            }
        }


        val manager = GridLayoutManager(activity,2, GridLayoutManager.VERTICAL,false)

        binding.groupList.layoutManager = manager
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.initialize()
        }
        binding.groupList.addOnScrollListener(object : EndlessScrollListener(manager) {
            override fun onLoadMore(currentPage: Int) {
                viewModel.loadMore(currentPage)
            }
            //TODO 正しく数字がインクリメントされているか。
            //TODO EndressScrollListenerの内部保存された変数をどうやって初期化するのか？　→　他のリスナーを参考にした方が良いのでは？
        })
        /*binding.groupList.addOnScrollListener(object:  RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.fetchNextPage()
                }
            }
        })*/
        //TODO swipeRefreshLayoutで不足していたら、ConstraintLayoutを追加する。

        //TODO 最下部まで行ったら、再度値を取得するように
        //TODO ある程度の数字まで行ったら、フィルターをするように促す。

        return binding.root
    }
}