package com.blueray.fares.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.fares.R
import com.blueray.fares.api.FollowerClick
import com.blueray.fares.databinding.FollowersItemBinding
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.model.FollowingList
import com.blueray.fares.model.FollowingResponse
import com.blueray.fares.model.VideoResponse
import com.bumptech.glide.Glide

class FollowersAdapter (
    // todo change list model
   var context: Context,
    var list : List<FollowingList>,
    var followClikc : FollowerClick
)
    : RecyclerView.Adapter<FollowersAdapter.MyViewHolder>() {


    inner class MyViewHolder(val binding : FollowersItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FollowersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(list[position].picture).placeholder(R.drawable.logo).into(holder.binding.profileImage)

        holder.binding.name.text = list[position].user_name
        holder.binding.username.hide()

        holder.binding.username.setBackgroundColor(Color.alpha(R.color.lightGreen))

if (list[position].flag == 1){
    holder.binding.follow.text = "الغاء المتابعة"
    holder.binding.follow.setBackgroundResource(R.drawable.un_follow)
    list[position].flag = 0
}else {
    holder.binding.follow.text = "متابعة"
    holder.binding.follow.setBackgroundResource(R.drawable.btnfollow)
    list[position].flag = 1


}
        holder.binding.follow.setOnClickListener {

            if (list[position].flag == 1){
                holder.binding.follow.text = "الغاء المتابعة"
                holder.binding.follow.setBackgroundResource(R.drawable.un_follow)

                list[position].flag = 0

            }else {
                holder.binding.follow.text = "متابعة"
                holder.binding.follow.setBackgroundResource(R.drawable.btnfollow)
                list[position].flag = 1


            }
            followClikc.onFollowClikcs(position)




        }

    }



}