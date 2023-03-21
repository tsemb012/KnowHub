package com.example.droidsoftthird

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetView
import java.util.UUID
import org.jitsi.meet.sdk.BroadcastEvent

//TODO チャットIDを発行して、それを元にチャットを開始するようにする。
class VideoChatFragment : Fragment() {
    private var jitsiMeetView: JitsiMeetView? = null
    private val eventId by lazy { VideoChatFragmentArgs.fromBundle(requireArguments()).eventId }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(VideoChatFragmentDirections.actionVideoChatFragmentToScheduleDetailFragment(eventId))
        }

        requireActivity().setContent {
            VideoChatView()
        }
    }

    @Composable
    fun VideoChatView() {
        val context = requireContext()
        AndroidView(factory = {
            JitsiMeetView(context).apply {
                jitsiMeetView = this
                val roomName = UUID.randomUUID().toString()
                val options = JitsiMeetConferenceOptions.Builder()
                    .setRoom(roomName)
                    .build()
                join(options)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        jitsiMeetView?.dispose()
        jitsiMeetView = null

        JitsiMeetActivityDelegate.onHostDestroy(requireActivity())
    }

    override fun onResume() {
        super.onResume()
        JitsiMeetActivityDelegate.onHostResume(requireActivity())
    }

    override fun onPause() {
        super.onPause()
        JitsiMeetActivityDelegate.onHostPause(requireActivity())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
