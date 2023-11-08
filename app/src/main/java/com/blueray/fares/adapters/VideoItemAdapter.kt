package com.blueray.fares.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.fares.databinding.ActivitiesCountItemsBinding
import com.blueray.fares.databinding.VideoListItemBinding

class VideoItemAdapter
    (var list: List<String>,
var listener : (id : String )-> Unit
): RecyclerView.Adapter<VideoItemAdapter.MyViewHolder>()
{

    inner class MyViewHolder(val binding : VideoListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = VideoListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = 20

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val data = list[position]
        val context = holder.itemView.context
        holder.apply {

        }
        holder.itemView.setOnClickListener {
            listener.invoke("")
        }
    }
}