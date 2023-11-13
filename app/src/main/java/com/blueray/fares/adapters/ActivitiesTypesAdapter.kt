package com.blueray.fares.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blueray.fares.databinding.ActivitiesCountItemsBinding

class ActivitiesTypesAdapter(
    var list: List<String>,
    var listener : (id : String )-> Unit
): RecyclerView.Adapter<ActivitiesTypesAdapter.MyViewHolder>()
{

    inner class MyViewHolder(val binding : ActivitiesCountItemsBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ActivitiesCountItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val data = list[position]
        val context = holder.itemView.context
        holder.apply {
//            binding.choice.text = list[position]
            binding.choice.setOnCheckedChangeListener { chip, isChecked ->
                // Responds to chip checked/unchecked
                // todo uncomment
//                if(isChecked){
//                    selected_items.add(list[position])
//                }else{
//                    for (i in selected_items.indices){
//                        if(list[position] == selected_items[i]){
//                            selected_items.removeAt(i)
//                        }
//                    }
//                }
            }
        }
        holder.itemView.setOnClickListener {
            listener
        }
    }

    companion object {
        var selected_items = mutableListOf<String>()
    }

}