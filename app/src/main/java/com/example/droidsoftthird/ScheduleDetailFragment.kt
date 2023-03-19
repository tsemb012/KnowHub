package com.example.droidsoftthird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleDetailFragment: Fragment() {

    @Inject
    lateinit var viewModelAssistedFactory: ScheduleDetailViewModel.Factory

    private val eventId by lazy { ScheduleDetailFragmentArgs.fromBundle(requireArguments()).eventId }

    private val viewModel by lazy { viewModelAssistedFactory.create(eventId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initialize()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ScheduleDetailScreen(viewModel)
            }
        }
    }

    @Composable
    private fun ScheduleDetailScreen(viewModel: ScheduleDetailViewModel) {




    }


    //DONE データクラスを修正 + 統合できるように　+ Idを取得できるように
    //DONE 画面遷移 scheduleIdを取得できるように
    //DONE ViewModelの作成
    //TODO ユースケケースにメソッドを追加
    //TODO Repositoryの作成
    //TODO idを渡してFetchできるように修正　→　関連するロケーションも取得
    //TODO viewModelまで値が持ってこれることを確認する。
    //TODO 基本的には会社で作った箇所を参考にするように jetpackComposeで描画　→　地図とオンラインで条件分岐をするようにする。
}


