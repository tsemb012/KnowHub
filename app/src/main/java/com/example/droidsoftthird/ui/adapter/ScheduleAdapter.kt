package com.example.droidsoftthird

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.droidsoftthird.databinding.ListItemGroupBinding
import com.example.droidsoftthird.databinding.ListItemScheduleBinding
import com.example.droidsoftthird.model.Group
import com.example.droidsoftthird.model.Schedule
import timber.log.Timber
import java.time.LocalDate

class ScheduleAdapter(val clickListener: ScheduleListener): ListAdapter<Schedule, ScheduleAdapter.ViewHolder>(ScheduleDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ScheduleAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position)!!,clickListener)
    }

    class ViewHolder private constructor(val binding: ListItemScheduleBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Schedule, clickListener: ScheduleListener){
            binding.schedule = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemScheduleBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ScheduleDiffCallback : DiffUtil.ItemCallback<Schedule>() {
    override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem.toString() == newItem.toString()//UniqueIDはDocument名に付与されているので、toString()で照合する。
    }

    override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem == newItem
    }
}

class ScheduleListener(val clickListener:(scheduleId:String) -> Unit){
    fun onClick(schedule:Schedule) { clickListener(schedule.scheduleId.toString())}
}