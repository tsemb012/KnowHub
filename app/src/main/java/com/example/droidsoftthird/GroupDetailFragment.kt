package com.example.droidsoftthird

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.lifecycle.ViewModelFactoryModules_ActivityModule_ProvideFactoryFactory.provideFactory
import androidx.hilt.lifecycle.ViewModelFactoryModules_FragmentModule_ProvideFactoryFactory.provideFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavArgs
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.droidsoftthird.databinding.FragmentGroupDetailBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.security.acl.Group
import javax.inject.Inject

@AndroidEntryPoint
class GroupDetailFragment : Fragment() {

    //DONE UIを洗練させる
    //DONE ツールバータイトルの良い表示方法を検討する。
    //DONE getGroup()の記述位置があっているか確認する。
    //DONE FloatingFabのOnClickロジック及び設計を考える。
    //TODO userIcon一覧を表示する。
    //DONE Chatからアクセスした場合、参加ボタンを非表示にする。


    @Inject
    lateinit var groupDetailViewModelAssistedFactory: GroupDetailViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentGroupDetailBinding = inflate(
            inflater, R.layout.fragment_group_detail, container, false
        )

        //カスタムActionBarなので、Activityが司るactionBarを非表示にして、ここで実装している。
        //-----ViewObjects for Navigation
        val layout: CollapsingToolbarLayout = binding.collapsingToolbarLayout
        val toolbar: Toolbar = binding.materialToolbar

        //-----NavUI Objects
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController
        val appBarConfiguration = AppBarConfiguration.Builder(navController.getGraph()).build()

        //-----Setup for NavigationUI
        NavigationUI.setupWithNavController(
            layout,
            toolbar,
            navController,
            appBarConfiguration
        )

        val groupId = GroupDetailFragmentArgs.fromBundle(requireArguments()).groupId
        val viewModel = groupDetailViewModelAssistedFactory.create(groupId)//自動生成されたFactoryにIdを入れて、ViewModelを生成

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.group.observe(viewLifecycleOwner, Observer {
            if(it?.members?.contains(FirebaseAuth.getInstance().uid) == true){
                binding.floatingBtnAdd.visibility = View.GONE //TODO 　表示を消すだけではなく、機能も使えなくしときたい。
            }
        })

        viewModel.confirmJoin.observe(viewLifecycleOwner,EventObserver{
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.confrimation)
                .setMessage(R.string.confrimation_join_group)
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.userJoinGroup()
                }
                .setNeutralButton(R.string.cancel) { _, _ ->
                }
                .show()
        })

        viewModel.navigateToMyPage.observe(viewLifecycleOwner, Observer { groupId ->
            groupId?.let {
                this.findNavController().navigate(
                    GroupDetailFragmentDirections.actionGroupDetailFragmentToMyPageFragment()
                )
                viewModel.onMyPageNavigated()
            }
        })
        return binding.root
    }

    companion object {
        private val TAG: String? = GroupDetailFragment::class.simpleName
    }
}
