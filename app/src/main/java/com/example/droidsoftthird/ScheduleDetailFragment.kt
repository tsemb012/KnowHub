package com.example.droidsoftthird

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.droidsoftthird.composable.event.EventDetailScreen
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

        viewModel.fetchEventDetail()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.message.let { message ->
            if (message.value != null) {
                Toast.makeText(requireContext(), message.value, Toast.LENGTH_SHORT).show()
            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                EventDetailScreen(
                        eventViewModel = viewModel,
                        startVideoChat = { startVideoChat()},
                        onBack = { findNavController().navigateUp() }
                )
            }
        }
    }

    private fun startVideoChat() {
        val intent = Intent(requireActivity(), VideoChatActivity::class.java)
        startActivity(intent)
    }


    //DONE データクラスを修正 + 統合できるように　+ Idを取得できるように
    //DONE 画面遷移 scheduleIdを取得できるように
    //DONE ViewModelの作成
    //DONE ユースケケースにメソッドを追加
    //DONE Repositoryの作成
    //TODO idを渡してFetchできるように修正　→　関連するロケーションも取得
    //TODO viewModelまで値が持ってこれることを確認する。
    //DONE 基本的には会社で作った箇所を参考にするように jetpackComposeで描画　→　地図とオンラインで条件分岐をするようにする。
}


