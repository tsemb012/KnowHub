package com.example.droidsoftthird

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.droidsoftthird.databinding.ListItemGroupBinding
import com.example.droidsoftthird.model.Group
import timber.log.Timber


class GroupAdapter(val clickListener: GroupListener): ListAdapter<Group, GroupAdapter.ViewHolder>(GroupDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {//This function takes two parameters and returns a ViewHolder.
        return ViewHolder.from(parent)
        //空っぽのViewHolderをCompanionObjectから引っ張ってくる。
        //普通にコンストラクターに引数を渡してインスタンス化しても良いが、コードが綺麗じゃない気がする。
        //fromから引っ張ってきた方がまとまりとして理解しやすい。
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!,clickListener)//作ったViewHolderに値を入れて、bindする。
        Timber.tag("getItem(position)").d(getItem(position).toString())
    }

    class ViewHolder private constructor(val binding: ListItemGroupBinding): RecyclerView.ViewHolder(binding.root){

        //TODO ListItem for Groupを完成させる。 デザイン設計　→　データ設計　
        //TODO ListItemにデータを分散して適応させる。BindApdaterを用いてデータをさらに加工させる。
        //DONE Firebaseからデータを取得する処理を実行する。
        //DONE Entityの構造については、Firebaseを実装する際に改める。

        fun bind(item: Group, clickListener: GroupListener){
            binding.group = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val binding =
                    ListItemGroupBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class GroupDiffCallback : DiffUtil.ItemCallback<Group>() {
    override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
        return oldItem.toString() == newItem.toString()//UniqueIDはDocument名に付与されているので、toString()で照合する。
    }

    override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
        return oldItem == newItem
    }
}

class GroupListener(val clickListener:(groupId:String, groupName:String) -> Unit){
    fun onClick(group: Group) {
        clickListener(group.groupId.toString(), group.groupName.toString())
    }
}






