package com.blueray.fares.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blueray.fares.api.OnCategroryChose
import com.blueray.fares.databinding.ActivitiesCountItemsBinding
import com.blueray.fares.model.DropDownModel

class ActivitiesTypesAdapter(
    var list: List<DropDownModel>,
    var onCategroryChose: OnCategroryChose
): RecyclerView.Adapter<ActivitiesTypesAdapter.MyViewHolder>()
{

    inner class MyViewHolder(val binding : ActivitiesCountItemsBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ActivitiesCountItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = list[position]
      holder.binding.choice.text = data.name
        val context = holder.itemView.context
        holder.apply {
//            binding.choice.text = list[position]
            binding.choice.setOnCheckedChangeListener { chip, isChecked ->
                // Responds to chip checked/unchecked
                // todo uncomment
                if(isChecked){
                    selected_items.add(list[position].id)

                    Log.d("1234567890", selected_items.size.toString())
                }else{

//                    for (i in selected_items.indices){
//                        if(list[position].id == selected_items[i]){
//                            selected_items.removeAt(i)
//                        }
//                    }
                    Log.d("123456789011", selected_items.size.toString())

                }
            }
        }
        holder.binding.choice.setOnClickListener {
            onCategroryChose.onCategroyChose(selected_items.joinToString(","))
        }
    }

    companion object {
        var selected_items = mutableListOf<String>()
    }

}