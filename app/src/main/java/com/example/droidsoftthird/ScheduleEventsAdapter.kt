package com.example.droidsoftthird

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.droidsoftthird.databinding.ListItemScheduleEventBinding
import com.example.droidsoftthird.model.domain_model.fire_model.FireScheduleEvent

class ScheduleEventsAdapter(val clickListener: () -> Unit): ListAdapter<FireScheduleEvent, ScheduleEventsAdapter.ViewHolder>(ScheduleEventDiffCallback()) {
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
        fun bind(item: FireScheduleEvent, clickListener: () -> Unit) {
            with(binding){
                scheduleEventItem.setOnClickListener { clickListener }
                /*eventDayOfWeek.text = item.date
                eventDate.text = item.date*/
                eventTitle.text = item.title
                /*eventTime.text = binding.root.gs(R.string.event_time,item.startTime.toString(), item.endTime.toString())
                eventPlace.text = item.place.  TODO Firebaseから受け取るデータの型が決定してから詰めていく。*/
                executePendingBindings()
            }
        }
    }
}

class ScheduleEventDiffCallback : DiffUtil.ItemCallback<FireScheduleEvent>() {

    override fun areItemsTheSame(oldItem: FireScheduleEvent, newItem: FireScheduleEvent): Boolean {
        return oldItem.toString() == newItem.toString()
    }
    override fun areContentsTheSame(oldItem: FireScheduleEvent, newItem: FireScheduleEvent): Boolean {
        return oldItem == newItem
    }
}
