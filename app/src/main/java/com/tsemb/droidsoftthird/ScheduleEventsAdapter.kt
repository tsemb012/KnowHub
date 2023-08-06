package com.tsemb.droidsoftthird

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.droidsoftthird.R
import com.example.droidsoftthird.databinding.ListItemScheduleEventBinding
import com.tsemb.droidsoftthird.extentions_depreciated.gs
import com.tsemb.droidsoftthird.model.domain_model.EventItem

class ScheduleEventsAdapter(private val onSelectEvent: (String) -> Unit): ListAdapter<EventItem, ScheduleEventsAdapter.ViewHolder>(
    ScheduleEventDiffCallback()
) {
    //TODO Hiltで関数をインジェクトする。
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(getItem(position), onSelectEvent) }

    class ViewHolder private constructor(val binding: ListItemScheduleEventBinding): RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from (parent: ViewGroup): ViewHolder {
                val layoutInflater= LayoutInflater.from(parent.context)
                val binding = ListItemScheduleEventBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
        fun bind(item: EventItem, onSelectEvent: (String) -> Unit) {
            with(binding){
                scheduleEventItem.setOnClickListener { onSelectEvent(item.eventId) }
                eventDayOfWeek.text = item.period.first.dayOfWeek.toString().substring(0,3)
                eventDate.text = item.period.first.toString()
                eventTitle.text = item.name
                eventTime.text = binding.root.gs(R.string.event_time,item.period.first.hour,item.period.second.hour)
                eventPlace.text = item?.placeName
                executePendingBindings()
            }
        }
    }
}

class ScheduleEventDiffCallback : DiffUtil.ItemCallback<EventItem>() {

    override fun areItemsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
        return oldItem.toString() == newItem.toString()
    }
    override fun areContentsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
        return oldItem == newItem
    }
}
