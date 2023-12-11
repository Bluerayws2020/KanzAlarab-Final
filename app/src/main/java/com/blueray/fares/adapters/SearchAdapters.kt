package com.blueray.fares.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.fares.R
import com.blueray.fares.api.OnProfileClick
import com.blueray.fares.api.OnProfileSearch
import com.blueray.fares.databinding.NotfiItemBinding
import com.blueray.fares.databinding.SearchItemBinding
import com.blueray.fares.model.SarchItem
import com.blueray.fares.model.SearchDataModel
import com.squareup.picasso.Picasso

class SearchAdapters(
    var list: List<SarchItem>,
    var onProfileSearch: OnProfileSearch
)
    : RecyclerView.Adapter<SearchAdapters.MyViewHolder>() {


    inner class MyViewHolder(val binding : SearchItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        TODO("Not yet implemented")
        holder.itemView.setOnClickListener {
            onProfileSearch.onProfileTargetSearch(position)
        }
        holder.binding.name.text = list[position].profile_data.first_name  + " " + list[position].profile_data.last_name
        holder.binding.username.text = list[position].user_name

        if (list[position].picture.isEmpty()){

        }else {
            Picasso.get()
                .load(list[position].picture)
                .placeholder(R.drawable.logo2)
                .error(R.drawable.logo2)
                .into(holder.binding.profileImage)
        }


    }



}