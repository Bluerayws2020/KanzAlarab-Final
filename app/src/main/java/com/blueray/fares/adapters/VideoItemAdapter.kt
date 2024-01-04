package com.blueray.fares.adapters

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blueray.fares.databinding.VideoListItemBinding
import com.blueray.fares.model.NewAppendItItems
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import com.arthenica.mobileffmpeg.FFmpeg
import com.blueray.fares.R
import com.blueray.fares.api.VideoClick
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.squareup.picasso.Picasso
//import pl.droidsonroids.gif.GifImageView
import java.io.BufferedReader
import java.io.InputStreamReader

class VideoItemAdapter(var  flag :Int,
    private var arrVideo: List<NewAppendItItems>,
    private var clikc:VideoClick,
   var context: Context,
    private var isLinearLayout: Boolean = false

) : RecyclerView.Adapter<VideoItemAdapter.MyViewHolder>() {
    fun setLinearLayoutMode(enabled: Boolean) {
        isLinearLayout = enabled
    }
    inner class MyViewHolder(val binding: VideoListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = VideoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = arrVideo.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            clikc.OnVideoClic(position)

        }
        val videoItem = arrVideo[position]
//        val videoPath = videoItem.videoUrl




        holder.apply {

            if (flag == 1){
                binding.statcard.hide()
                binding.statcard.hide()

                binding.txt.text = videoItem.status
                if ( videoItem.status == "published"){
                    binding.statcard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
                }else {
                    binding.statcard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))

                }


            }else {

            }


            Log.d("TEssssImage",videoItem.imageThum)

//            if (i)
//            Glide.with(binding.gifs.context).load(videoItem.imageThum).into(binding.gifs)

            if (videoItem.imageThum.isNullOrEmpty()){
                Log.d("ERRROR","1")
            }else {

                Picasso.get()
                    .load(videoItem.imageThum)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(binding.gifs)

            }



//            if (isLinearLayout) {
//                binding.gifs.setVideoPath(videoItem.videoUrl)
//                holder.binding.times.hide()
//                binding.gifs.setOnPreparedListener { mp ->
//
//                    mp.start()
//
//                }
//            } else {
//                binding.gifs.setVideoPath(videoItem.videoUrl)
                holder.binding.times.text  = videoItem.duration.toString()
////                Glide.with(binding.imgs.context)
//            }

        }
    }



}
