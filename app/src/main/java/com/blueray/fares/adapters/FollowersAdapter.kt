package com.blueray.fares.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.fares.databinding.FollowersItemBinding
import com.blueray.fares.databinding.NotfiItemBinding

class FollowersAdapter (
    // todo change list model
    var list : List<String>
)
    : RecyclerView.Adapter<FollowersAdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding : FollowersItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FollowersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = 20 // todo list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        TODO("Not yet implemented")
    }


}