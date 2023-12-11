package com.blueray.fares.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.fares.databinding.NotfiItemBinding
import com.blueray.fares.databinding.SupportItemBinding
import com.blueray.fares.model.VideoResponse


class SupportAdapter // this down is the basic implementation of an adapter
    (
    // todo change list model
    var list: List<String>
)
    : RecyclerView.Adapter<SupportAdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding : SupportItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SupportItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)


        return MyViewHolder(binding)
    }
    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        TODO("Not yet implemented")


//        holder.binding.name.text =  list[position]
    }


}