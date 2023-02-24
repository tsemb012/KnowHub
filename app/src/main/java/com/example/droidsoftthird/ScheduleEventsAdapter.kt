package com.example.droidsoftthird

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.droidsoftthird.databinding.ListItemScheduleEventBinding
import com.example.droidsoftthird.model.domain_model.ScheduleEventForHome

class ScheduleEventsAdapter(val clickListener: () -> Unit): ListAdapter<ScheduleEventForHome, ScheduleEventsAdapter.ViewHolder>(ScheduleEventDiffCallback()) {
    //TODO Hiltで関数をインジェクトする。
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(getItem(position), clickListener) }

    class ViewHolder private constructor(val binding: ListItemScheduleEventBinding): RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from (parent: ViewGroup): ViewHolder {
                val layoutInflater= LayoutInflater.from(parent.context)
                val binding = ListItemScheduleEventBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
        fun bind(item: ScheduleEventForHome, clickListener: () -> Unit) {
            with(binding){
                scheduleEventItem.setOnClickListener { clickListener }
                /*eventDayOfWeek.text = item.date
                eventDate.text = item.date*/
                eventTitle.text = item.name
                /*eventTime.text = binding.root.gs(R.string.event_time,item.startTime.toString(), item.endTime.toString())
                eventPlace.text = item.place.  TODO Firebaseから受け取るデータの型が決定してから詰めていく。*/
                executePendingBindings()
            }
        }
    }
}

class ScheduleEventDiffCallback : DiffUtil.ItemCallback<ScheduleEventForHome>() {

    override fun areItemsTheSame(oldItem: ScheduleEventForHome, newItem: ScheduleEventForHome): Boolean {
        return oldItem.toString() == newItem.toString()
    }
    override fun areContentsTheSame(oldItem: ScheduleEventForHome, newItem: ScheduleEventForHome): Boolean {
        return oldItem == newItem
    }
}
