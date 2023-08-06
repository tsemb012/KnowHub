package com.tsemb.droidsoftthird

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.facebook.react.modules.core.PermissionListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jitsi.meet.sdk.*


//TODO チャットIDを発行して、それを元にチャットを開始するようにする。
class VideoChatActivity : AppCompatActivity(), JitsiMeetActivityInterface {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
    private var jitsiMeetView: JitsiMeetView? = null
    private val roomId: String by lazy { intent.getStringExtra("roomId") ?: throw IllegalStateException() }
    private val broadcastReceiver by lazy {
        object : BroadcastReceiver(this) {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    when (it.action) {
                        BroadcastEvent.Type.CONFERENCE_TERMINATED.action -> {
                            // ここで終了ボタンがクリックされたときの処理を実行します
                            Log.d("tsemb012", "onReceive:")
                            finish()
                        }
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentFilter = IntentFilter()
        intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.getAction())
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)

        setContent {
            VideoChatView()
        }
    }

    override fun onBackPressed() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setMessage("本当に終了してよろしいですか？")
            .setPositiveButton("はい") { _, _ ->
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(BroadcastIntentHelper.buildHangUpIntent())
                super.onBackPressed()
            }
            .setNegativeButton("いいえ") { dialog, _ ->
                dialog.dismiss()
            }
        builder.show()
    }

    @Composable
    fun VideoChatView() {
        val context = this@VideoChatActivity
        AndroidView(factory = {
            JitsiMeetView(context).apply {
                jitsiMeetView = this
            }
        })
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        this.requestPermissions(permissions, PERMISSION_REQUEST_CODE)
    }

    private fun setupVideoChat() {
        val options = JitsiMeetConferenceOptions.Builder()
            .setRoom(roomId)
            .build()
        jitsiMeetView?.join(options)
    }


    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)

        jitsiMeetView?.dispose()
        jitsiMeetView = null

        JitsiMeetActivityDelegate.onHostDestroy(this)
        finish()
    }

    override fun onResume() {
        super.onResume()
        JitsiMeetActivityDelegate.onHostResume(this)
    }

    override fun onPause() {
        super.onPause()
        JitsiMeetActivityDelegate.onHostPause(this)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(BroadcastIntentHelper.buildHangUpIntent())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // すべてのパーミッションが許可された場合、ビデオチャットの準備を開始する
                setupVideoChat()
            } else {
                // 必要なパーミッションが許可されなかった場合、エラーメッセージを表示する
                Toast.makeText(this, "Permissions not granted. Video chat cannot proceed.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )

        val permissionsToRequest = permissions.filter { ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            // すべてのパーミッションが許可されている場合、ビデオチャットの準備を開始する
            setupVideoChat()
        }
    }
}
