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

@AndroidEntryPoint//Enable this class to receive dependency from Hilt //MainActivityに依存するFragmentにもエントリーポイントを付与する必要がある。
class RecommendPagerFragment:Fragment() {

    private val viewModel:RecommendPagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentPagerRecommendBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pager_recommend, container, false)

        binding.recommendPagerViewModel = viewModel//bindingにViewModelをぶち込んで、レイアウトにデータを表示させる。
        binding.lifecycleOwner = viewLifecycleOwner//bindingにもライフサイクルを適用させて、LiveDataを有効にする。

        /*
        * val groupListener = GroupListener{groupId,_ -> viewModel.onGroupClicked(groupId)}// GroupAdapter内のクラス設計図を使って、 インスタンス化。クラス内のメソッドは1つで受け取る引数は決まっている。
        * val adapter = GroupAdapter(groupListener)　//こちら側で変更させるのは、onClickされた後の関数。//もっと可読性の高い方法があるんじゃないの？
        * と分解した方がわかりやすい。
        * */

        val adapter = GroupAdapter(GroupListener{ groupId,_ -> //将来的に、ここでインスタンス化するのではなく、Hiltにインスタンス化の処理を任せたい。
            viewModel.onGroupClicked(groupId)//Layoutに画面遷移に至るまで過程を完全にレイアウトに委譲している。
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