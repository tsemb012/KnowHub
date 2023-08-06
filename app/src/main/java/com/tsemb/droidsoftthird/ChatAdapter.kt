package com.tsemb.droidsoftthird

/*MIT License

Copyright (c) 2020 Satyamurti Doddini

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.*/

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.droidsoftthird.R
import com.tsemb.droidsoftthird.ChatAdapter.Companion.messageList
import com.example.droidsoftthird.databinding.*
import com.tsemb.droidsoftthird.utils.AuthUtil
import com.tsemb.droidsoftthird.model.domain_model.fire_model.FileMessage
import com.tsemb.droidsoftthird.model.domain_model.fire_model.FireMessage
import com.tsemb.droidsoftthird.model.domain_model.fire_model.ImageMessage
import com.tsemb.droidsoftthird.model.domain_model.fire_model.RecordMessage
import com.tsemb.droidsoftthird.model.domain_model.fire_model.TextMessage
import com.tsemb.droidsoftthird.utils.UpdateRecycleItemEvent
import java.io.IOException
import kotlin.properties.Delegates

var positionDelegate: Int by Delegates.observable(-1){prop,old,new ->
    println("<positionDelegate>.:${old},,,,$new")
    if(old != new && old != -1)
        org.greenrobot.eventbus.EventBus.getDefault().post(UpdateRecycleItemEvent(old))//TODO CoroutineFlowに置き換える。
}

class ChatAdapter(private val context: Context?, private val clickListener: MessageClickListener):
    ListAdapter<FireMessage, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    override fun getItemViewType(position: Int): Int {

        val currentMessage = getItem(position)

        return when{

            currentMessage.userId == AuthUtil.getAuthId() && currentMessage.messageType == 0.0 ->{
                 TYPE_SENT_MESSAGE
            }
            currentMessage.userId != AuthUtil.getAuthId() && currentMessage.messageType == 0.0 ->{
                TYPE_RECEIVED_MESSAGE
            }
            currentMessage.userId == AuthUtil.getAuthId() && currentMessage.messageType == 1.0 ->{
                TYPE_SENT_IMAGE_MESSAGE
            }
            currentMessage.userId != AuthUtil.getAuthId() && currentMessage.messageType == 1.0 ->{
                TYPE_RECEIVED_IMAGE_MESSAGE
            }
            currentMessage.userId == AuthUtil.getAuthId() && currentMessage.messageType == 2.0 ->{
                TYPE_SENT_FILE_MESSAGE
            }
            currentMessage.userId != AuthUtil.getAuthId() && currentMessage.messageType == 2.0 ->{
                TYPE_RECEIVED_FILE_MESSAGE
            }
            currentMessage.userId == AuthUtil.getAuthId() && currentMessage.messageType == 3.0 ->{
                TYPE_SENT_RECORD
            }
            currentMessage.userId != AuthUtil.getAuthId() && currentMessage.messageType == 3.0 ->{
                TYPE_RECEIVED_RECORD
            }
            currentMessage.messageType == 8.0 -> {
                TYPE_SENT_RECORD_PLACEHOLDER
            }
            else -> {
                throw IllegalArgumentException("Invalid ItemViewType")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return when (viewType){

            TYPE_SENT_MESSAGE -> {
                SentMessageViewHolder.from(parent)
            }

            TYPE_RECEIVED_MESSAGE -> {
                ReceivedMessageViewHolder.from(parent)
            }

            TYPE_SENT_IMAGE_MESSAGE -> {
                SentImageMessageViewHolder.from(parent)
            }

            TYPE_RECEIVED_IMAGE_MESSAGE -> {
                ReceivedImageMessageViewHolder.from(parent)
            }

            TYPE_SENT_FILE_MESSAGE -> {
                SentFileMessageViewHolder.from(parent)
            }

            TYPE_RECEIVED_FILE_MESSAGE -> {
                ReceivedFileMessageViewHolder.from(parent)
            }

            TYPE_SENT_RECORD -> {
                SentRecordMessageViewHolder.from(parent)
            }

            TYPE_RECEIVED_RECORD -> {
                ReceivedRecordMessageViewHolder.from(parent)
            }

            TYPE_SENT_RECORD_PLACEHOLDER -> {
                SentRecordPlaceHolderViewHolder.from(parent)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SentMessageViewHolder -> {
                holder.bind(clickListener, getItem(position) as TextMessage)
            }
            is ReceivedMessageViewHolder -> {
                holder.bind(clickListener, getItem(position) as TextMessage)
            }
            is SentImageMessageViewHolder -> {
                holder.bind(clickListener, getItem(position) as ImageMessage)
            }
            is ReceivedImageMessageViewHolder -> {
                holder.bind(clickListener, getItem(position) as ImageMessage)
            }
            is ReceivedFileMessageViewHolder -> {
                holder.bind(clickListener, getItem(position) as FileMessage)
            }
            is SentFileMessageViewHolder -> {
                holder.bind(clickListener, getItem(position) as FileMessage)
            }
            is SentRecordMessageViewHolder -> {
                holder.bind(clickListener, getItem(position) as RecordMessage)
            }
            is ReceivedRecordMessageViewHolder -> {
                holder.bind(clickListener, getItem(position) as RecordMessage)
            }
            is SentRecordPlaceHolderViewHolder -> {
                holder.bind(clickListener, getItem(position) as RecordMessage)
            }
            else -> throw IllegalArgumentException("Invalid ViewHolder type")
        }
    }


    companion object {
        private const val TYPE_SENT_MESSAGE = 0
        private const val TYPE_RECEIVED_MESSAGE = 1
        private const val TYPE_SENT_IMAGE_MESSAGE = 2
        private const val TYPE_RECEIVED_IMAGE_MESSAGE = 3
        private const val TYPE_SENT_FILE_MESSAGE = 4
        private const val TYPE_RECEIVED_FILE_MESSAGE = 5
        private const val TYPE_SENT_RECORD = 6
        private const val TYPE_RECEIVED_RECORD = 7
        private const val TYPE_SENT_RECORD_PLACEHOLDER = 8
        lateinit var messageList: MutableList<FireMessage>
    }


}
//TYPE_SENT_MESSAGE = 0
class SentMessageViewHolder private constructor(val binding:SentTextItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: MessageClickListener, item: TextMessage){
        binding.message = item
        binding.clickListener = clickListener
        binding.position = adapterPosition
        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): SentMessageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SentTextItemBinding.inflate(layoutInflater, parent, false)
            return SentMessageViewHolder(binding)
        }
    }
}

//TYPE_RECEIVED_MESSAGE = 1
class ReceivedMessageViewHolder private constructor(val binding:ReceivedMessageItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: MessageClickListener, item: TextMessage) {
        binding.message = item
        binding.clickListener = clickListener
        binding.position = adapterPosition
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ReceivedMessageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ReceivedMessageItemBinding.inflate(layoutInflater, parent, false)
            return ReceivedMessageViewHolder(binding)
        }
    }
}

//TYPE_SENT_IMAGE_MESSAGE = 2
class SentImageMessageViewHolder private constructor(val binding:SentImageItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: MessageClickListener, item: ImageMessage){
        binding.message = item
        binding.clickListener = clickListener
        binding.position = adapterPosition
        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): SentImageMessageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SentImageItemBinding.inflate(layoutInflater, parent, false)
            return SentImageMessageViewHolder(binding)
        }
    }
}

//TYPE_RECEIVED_IMAGE_MESSAGE = 3
class ReceivedImageMessageViewHolder private constructor(val binding:ReceivedImageItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: MessageClickListener, item: ImageMessage){
        binding.message = item
        binding.clickListener = clickListener
        binding.position = adapterPosition
        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): ReceivedImageMessageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ReceivedImageItemBinding.inflate(layoutInflater, parent, false)
            return ReceivedImageMessageViewHolder(binding)
        }
    }
}

//TYPE_SENT_FILE_MESSAGE = 4
class SentFileMessageViewHolder private constructor(val binding: SentFileItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: MessageClickListener, item: FileMessage){
        binding.message = item
        binding.clickListener = clickListener
        binding.position = adapterPosition
        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): SentFileMessageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SentFileItemBinding.inflate(layoutInflater, parent, false)
            return SentFileMessageViewHolder(binding)
        }
    }
}

//TYPE_RECEIVED_FILE_MESSAGE = 5
class ReceivedFileMessageViewHolder private constructor(val binding: ReceivedFileItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: MessageClickListener, item: FileMessage){
        binding.message = item
        binding.clickListener = clickListener
        binding.position = adapterPosition
        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): ReceivedFileMessageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ReceivedFileItemBinding.inflate(layoutInflater, parent, false)
            return ReceivedFileMessageViewHolder(binding)
        }
    }
}

//TYPE_SENT_RECORD = 6
class SentRecordMessageViewHolder private constructor(val binding: SentRecordItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: MessageClickListener, item: RecordMessage) {
        binding.message = item
        binding.clickListener = clickListener
        binding.position = adapterPosition
        binding.executePendingBindings()

        //reset views(to reset other records other than the one playing)
        val recordMessage = messageList[adapterPosition] as RecordMessage
        recordMessage.isPlaying = false
        binding.playPauseImage.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        binding.progressBar.max = 0
        binding.playPauseImage.setOnClickListener {
            startPlaying(
                item.voiceRef!!,
                adapterPosition,
                recordMessage,
                binding.playPauseImage,
                binding.progressBar
            )
        }
    }

    companion object{
        fun from(parent: ViewGroup): SentRecordMessageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SentRecordItemBinding.inflate(layoutInflater, parent, false)
            return SentRecordMessageViewHolder(binding)
        }
    }
}

//TYPE_RECEIVED_RECORD = 7
class ReceivedRecordMessageViewHolder private constructor(val binding: ReceivedRecordItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: MessageClickListener, item: RecordMessage) {
        binding.message = item
        binding.clickListener = clickListener
        binding.position = adapterPosition
        binding.executePendingBindings()

        //reset views(to reset other records other than the one playing)
        val recordMessage = messageList[adapterPosition] as RecordMessage
        recordMessage.isPlaying = false
        binding.playPauseImage.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        binding.progressBar.max = 0
        binding.playPauseImage.setOnClickListener {
            startPlaying(
                item.voiceRef!!,
                adapterPosition,
                recordMessage,
                binding.playPauseImage,
                binding.progressBar
            )
        }
    }

    companion object{
        fun from(parent: ViewGroup): ReceivedRecordMessageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ReceivedRecordItemBinding.inflate(layoutInflater, parent, false)
            return ReceivedRecordMessageViewHolder(binding)
        }
    }
}

//8
class SentRecordPlaceHolderViewHolder private constructor(val binding: SentRecordPlaceholderItemBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(clickListener: MessageClickListener, item: FireMessage) {
        binding.message = item
        binding.clickListener = clickListener
        binding.position = adapterPosition
        binding.executePendingBindings()

    }

    companion object {
        fun from(parent: ViewGroup): SentRecordPlaceHolderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SentRecordPlaceholderItemBinding.inflate(layoutInflater, parent, false)
            return SentRecordPlaceHolderViewHolder(binding)
        }
    }
}

private var player = MediaPlayer()
private lateinit var countDownTimer: CountDownTimer

private fun startPlaying(
    audioUri: String,
    adapterPosition: Int,
    recordMessage: RecordMessage,
    playPauseImage: ImageView,
    progressbar: ProgressBar
) {
    //Update last clicked item to be reset
    positionDelegate = adapterPosition

    //show temporary loading while audio is downloaded
    playPauseImage.setImageResource(R.drawable.loading_animation)

    if (recordMessage.isPlaying == null || recordMessage.isPlaying == false) {

        stopPlaying()
        recordMessage.isPlaying = false

        player.apply {
            try {
                setDataSource(audioUri)
                prepareAsync()
            } catch (e: IOException) {
                println("ChatFragment.startPlaying:prepare failed")
            }

            setOnPreparedListener {
                //media downloaded and will play

                recordMessage.isPlaying = true
                //play the record
                start()

                //change image to stop and show progress of record
                progressbar.max = player.duration
                playPauseImage.setImageResource(R.drawable.ic_stop_black_24dp)

                //count down timer to show record progress but on when record is playing
                countDownTimer = object : CountDownTimer(player.duration.toLong(), 50) {
                    override fun onFinish() {
                        progressbar.progress = (player.duration)
                        playPauseImage.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                    }

                    override fun onTick(millisUtilFinished: Long) {
                        progressbar.progress = (player.duration.minus(millisUtilFinished)).toInt()
                    }
                }.start()
            }
        }
    }else{
        //stop the record
        playPauseImage.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        stopPlaying()
        recordMessage.isPlaying = false
        progressbar.progress = 0
    }
}

private fun stopPlaying(){
    if(::countDownTimer.isInitialized)
        countDownTimer.cancel()
    player.reset()
}

class MessageDiffCallback : DiffUtil.ItemCallback<FireMessage>() {
    override fun areItemsTheSame(oldItem: FireMessage, newItem: FireMessage): Boolean {
        return oldItem.toString() == newItem.toString()
    }

    override fun areContentsTheSame(oldItem: FireMessage, newItem: FireMessage): Boolean {
        return oldItem.equals(newItem)
    }
}

interface MessageClickListener {
    fun onMessageClicked(position: Int, message: FireMessage)
}