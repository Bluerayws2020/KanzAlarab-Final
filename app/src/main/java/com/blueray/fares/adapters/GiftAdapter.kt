package com.blueray.fares.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blueray.fares.api.onGiftclicks
import com.blueray.fares.databinding.GridLayoutItemBinding
import com.blueray.fares.model.Grid


class GiftAdapter(
    var list: List<Grid>
    ,var onGiftclicks: onGiftclicks
):RecyclerView.Adapter<GiftAdapter.GridViewHolder>() {
    inner class GridViewHolder(val binding: GridLayoutItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
    val binding = GridLayoutItemBinding.inflate(LayoutInflater.from(parent.context) , parent ,false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            gridItemIv.setAnimation(item.image)
            gridItemIv.playAnimation()
            gridItemTv.text = item.name
            gridItemPrice.text = item.price
        }

        holder.binding.gridItemIv.setOnClickListener {
            onGiftclicks.onUserClickOnGift(position)
        }
    }

    override fun getItemCount(): Int = list.size
}