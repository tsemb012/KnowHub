package com.example.droidsoftthird

import android.Manifest
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.Activity
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.droidsoftthird.databinding.ChatRoomFragmentBinding
import com.example.droidsoftthird.model.*
import com.example.droidsoftthird.utils.UpdateRecycleItemEvent
import com.example.droidsoftthird.utils.showSnackbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.stfalcon.imageviewer.StfalconImageViewer
import com.stfalcon.imageviewer.loader.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.IOException
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {

    @Inject
    lateinit var chatRoomViewModelAssistedFactory: ChatRoomViewModel.Factory
    private lateinit var binding: ChatRoomFragmentBinding
    private lateinit var layout: View
    private lateinit var viewModel: ChatRoomViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<NestedScrollView>
    private lateinit var adapter: ChatAdapter
    private var messageList = mutableListOf<Message>()
    private var isRecording = false
    private var recorder: MediaRecorder? = null
    private var recordStart = 0L
    private var recordDuration = 0L
    private val navArgs by navArgs<ChatRoomFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val groupId = ChatRoomFragmentArgs.fromBundle(requireArguments()).groupId
        val groupName = ChatRoomFragmentArgs.fromBundle(requireArguments()).groupName

        binding = ChatRoomFragmentBinding.inflate(inflater, container, false)
        layout = binding.root
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        viewModel = chatRoomViewModelAssistedFactory.create(groupId)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //-----NavUI Objects
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController
        val appBarConfiguration = AppBarConfiguration.Builder(navController.getGraph()).build()

        //-----Setup for NavigationUI
        NavigationUI.setupWithNavController(
            binding.chatTitleToolbar,
            navController,
            appBarConfiguration
        )
        binding.chatTitleToolbar.title = groupName

        //-----Setup Adapter
        adapter = ChatAdapter(context, object: MessageClickListener{//ここでしか使わないクラスを無名クラス(オブジェクト)で作成。
            override fun onMessageClicked(position: Int, message: Message) {//端的には、メソッド渡したかったてだけでしょ。ただ、他にもっと良い方法がありそうなもんだけどね。
                when(message.messageType) {//発動するメソッドをメッセージのタイプによって条件分岐しているって感じ。
                    1.0 -> {
                        binding.fullSizeImageView.visibility = View.VISIBLE
                        StfalconImageViewer.Builder<MyImage>(
                            requireActivity(),
                            listOf(MyImage((message as ImageMessage).imageRef!!)),
                            ImageLoader<MyImage> { imageView, myImage ->
                                Glide.with(requireActivity())
                                    .load(FirebaseStorage.getInstance().getReference(myImage.url))
                                    .apply(RequestOptions().error(R.drawable.ic_broken_image_white_24dp))
                                    .into(imageView)
                            })
                            .withDismissListener { binding.fullSizeImageView.visibility = View.GONE }
                            .show()
                    }
                    2.0 -> {
                        val dialogBuilder = context?.let { it -> AlertDialog.Builder(it) }
                        dialogBuilder?.setMessage(R.string.download_clicked_file_or_not)
                            ?.setPositiveButton(R.string.yes) { _, _ ->
                                downloadFile(message)//TODO ここに問題あり、ダウンロードできないのは、適切パーミションを与えられていないからでは？要修正
                            }?.setNegativeButton(R.string.no, null)?.show()
                    }
                    3.0 -> {
                        adapter.notifyDataSetChanged()//Adapterにデータの変更を伝える。録音の際に使用。
                    }
                }
            }
        })
        binding.messageRecycler.adapter = adapter

        //-----Observe messages from Flow
        viewModel.messages.observe(viewLifecycleOwner, Observer{
            messageList = it as MutableList<Message>
            ChatAdapter.messageList = messageList //別でここに入れているということは？特別な役割があるはずでしょ。
            adapter.submitList(it)
            binding.messageRecycler.scrollToPosition(adapter.itemCount-1)
        })

        //------Observe user's actions
        viewModel.navigationToGroupDetail.observe(viewLifecycleOwner,EventObserver{
            findNavController().navigate(
                ChatRoomFragmentDirections.actionChatRoomFragmentToGroupDetailFragment(groupId))
        })
        viewModel.navigationToSchedule.observe(viewLifecycleOwner,EventObserver{
            findNavController().navigate(
                ChatRoomFragmentDirections.actionChatRoomFragmentToScheduleFragment(groupId))
        })
        viewModel.navigationToVideo.observe(viewLifecycleOwner,EventObserver{
            findNavController().navigate(
                ChatRoomFragmentDirections.actionChatRoomFragmentToVideoFragment(groupId))
        })
        viewModel.showBottomSheet.observe(viewLifecycleOwner,EventObserver{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        })
        viewModel.hideBottomSheet.observe(viewLifecycleOwner,EventObserver{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        })
        viewModel.attachmentOfImage.observe(viewLifecycleOwner,EventObserver{
            launchUploader(REQUEST_IMAGE_OPEN)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        })
        viewModel.attachmentOfFile.observe(viewLifecycleOwner,EventObserver{
            launchUploader(REQUEST_FILE_OPEN)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        })
        viewModel.recordingVoice.observe(viewLifecycleOwner,EventObserver{
            handleRecord()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRecycleItemEvent(event: UpdateRecycleItemEvent) {
        adapter.notifyItemChanged(event.adapterPosition)
    }//Coroutine Flowに置き換える

    //-----ConditionalOption when touched record button
    private fun handleRecord() {//まさにこういう部分をクラスデリゲーションしたい。
        if (isRecording) {//レコード中を停止している。//可読性をあげるような記述に変更したい。//本来であれば、ViewModelに置いておきたい。
            stopRecording()
            showPlaceholderRecord()//結構大事な役割というか、空データをリストに追加してるのにもかかわらず、それが伝わってこない。
            viewModel.createRecordMessage(//録音ファイルパスをFirebaseに保存している。
                "${requireActivity().externalCacheDir?.absolutePath}/audiorecord.3gp",
                recordDuration.toString(),
                )
            Toast.makeText(context, "録音が完了しました。", Toast.LENGTH_SHORT).show()
            isRecording = !isRecording //TrueからFalseに切り替えて、録音中ではないというステータスに変更する。
        } else {//録音スタートする時。
            when {//以下、パーミションを取得するための処理。
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO,) == PackageManager.PERMISSION_GRANTED -> {

                    layout.showSnackbar(
                        R.string.audio_permission_available,
                        Snackbar.LENGTH_INDEFINITE,
                        R.string.yes
                    ) {
                        startRecording()
                    }
                }

                shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {

                    layout.showSnackbar(
                        R.string.permisson_is_required,
                        Snackbar.LENGTH_INDEFINITE,
                        R.string.yes
                    ) {
                        requestAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }

                else -> {

                    layout.showSnackbar(
                        R.string.audio_permission_not_available,
                        Snackbar.LENGTH_LONG,
                        R.string.yes
                    ){
                        requestAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
            }
        }
    }

    private val requestAudioPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                layout.showSnackbar(R.string.audio_permission_granted, Snackbar.LENGTH_INDEFINITE, R.string.yes) {//パーミッションが許可されたとき。
                    startRecording()
                }
            } else {
                layout.showSnackbar(R.string.audio_permission_denied, Snackbar.LENGTH_SHORT, R.string.yes) //Permissionが拒否されたとき。
            }
        }

    //-----StartRecording
    private fun startRecording(){

        //-----Animate RecordButton
        val increaser = AnimatorInflater.loadAnimator(
            context,
            R.animator.increase_size
        ) as AnimatorSet
        increaser.setTarget(binding.recordVoiceFab)
        increaser.start()
        binding.recordVoiceFab.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor("#EE4B4B"))


        //-----Start Record
        val fileName = "${requireActivity().externalCacheDir?.absolutePath}/audiorecord.3gp"//name of the file where record will be stored
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
            }catch (e:IOException){
                println("ChatRoomFragment.startRecording${e.message}")
            }
            start()
            recordStart = Date().time
        }
        isRecording = !isRecording
    }

    private fun stopRecording(){

        //-----Animate RecordButton
        val regainer = AnimatorInflater.loadAnimator(
            context,
            R.animator.regain_size
        ) as AnimatorSet
        regainer.setTarget(binding.recordVoiceFab)
        regainer.start()
        binding.recordVoiceFab.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor("#b39ddb"))

        //-----stop Record
        recorder?.apply {
            stop()
            release()
            recorder = null
        }
        recordDuration = Date().time - recordStart
    }

    private fun showPlaceholderRecord(){
        messageList.add(
            RecordMessage(//空メッセージをリストに追加。
                FirebaseAuth.getInstance().uid,
                null,
                null,
                8.0,
                null,
                null,
                null,
                null,
                Date(),
                null,
            )
        )
        adapter.submitList(messageList)//修正したリストをアダプターに再提出。
        adapter.notifyItemInserted(messageList.size - 1)//adapterにデータが追加されたことを通知。
        binding.messageRecycler.scrollToPosition(messageList.size - 1)//スクロールの位置を変更
    }

    private fun launchUploader(request:Int) {
        when(request){
            REQUEST_IMAGE_OPEN ->{
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "image/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                }
                startActivityForResult(
                    intent,
                    request
                )
            }
            REQUEST_FILE_OPEN ->{
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                try {
                    startActivityForResult(intent, REQUEST_FILE_OPEN)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        context,
                        "No suitable file manager was found on this device",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            (requestCode == REQUEST_IMAGE_OPEN && resultCode == Activity.RESULT_OK)
            -> { val fullPhotoUri: Uri = requireNotNull(data?.data, { "requireNotNull" })
                viewModel.createImageMessage(fullPhotoUri)}

            (requestCode == REQUEST_FILE_OPEN && resultCode == Activity.RESULT_OK)
            -> { val fileUri: Uri = requireNotNull(data?.data, { "requireNotNull" })
                viewModel.createFileMessage(fileUri)}
        }
    }


    private fun downloadFile(message: Message) {

        //check for storage permission then download if granted
        Dexter.withActivity(requireActivity())
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    //download file
                    val downloadManager =
                        activity!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val uri = Uri.parse((message as FileMessage).fileRef)
                    val request = DownloadManager.Request(uri)
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        uri.lastPathSegment
                    )
                    downloadManager.enqueue(request)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                    //notify parent activity that permission denied to show toast for manual permission giving
                        layout.showSnackbar(
                            R.string.permisson_is_required,
                            Snackbar.LENGTH_INDEFINITE,
                            R.string.yes
                        )
                    }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    //notify parent activity that permission denied to show toast for manual permission giving
                        layout.showSnackbar(
                            R.string.permisson_is_denied,
                            Snackbar.LENGTH_INDEFINITE,
                            R.string.yes
                        )
                }
            }).check()
    }

    companion object {
        private const val REQUEST_IMAGE_OPEN = 101
        private const val REQUEST_FILE_OPEN  = 102
    }

}