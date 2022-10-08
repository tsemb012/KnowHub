package com.example.droidsoftthird

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.droidsoftthird.databinding.ListItemGroupBinding
import com.example.droidsoftthird.model.rails_model.ApiGroup

class GroupAdapter(val clickListener: GroupListener): ListAdapter<ApiGroup, GroupAdapter.ViewHolder>(GroupDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(getItem(position),clickListener) }

    class ViewHolder private constructor(val binding: ListItemGroupBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ApiGroup, clickListener: GroupListener) {
            binding.group = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemGroupBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class GroupDiffCallback : DiffUtil.ItemCallback<ApiGroup>() {
    override fun areItemsTheSame(oldItem: ApiGroup, newItem: ApiGroup): Boolean {
        return oldItem.toString() == newItem.toString()//UniqueIDはDocument名に付与されているので、toString()で照合する。
    }

    override fun areContentsTheSame(oldItem: ApiGroup, newItem: ApiGroup): Boolean {
        return oldItem == newItem
    }
}

class GroupListener(val clickListener:(groupId:String, groupName:String) -> Unit){
    fun onClick(group: ApiGroup) = clickListener(group.groupId.toString(), group.groupName.toString())
}
