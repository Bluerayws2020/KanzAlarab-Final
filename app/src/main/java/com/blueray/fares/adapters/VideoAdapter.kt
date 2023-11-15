package com.blueray.fares.adapters

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.blueray.fares.R
import com.blueray.fares.api.OnProfileClick
import com.blueray.fares.databinding.ItemVideoBinding
import com.blueray.fares.databinding.VideoListItemBinding
import com.blueray.fares.model.GenralVediosItem
import com.blueray.fares.model.NewAppendItItems

class VideoAdapter(arrVideo:ArrayList<NewAppendItItems>, private var onProfileClick: OnProfileClick) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    var arrVideoModel:ArrayList<NewAppendItItems> = arrVideo

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrVideoModel.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.setVideoData(arrVideoModel[position])
holder.binding.profiel.setOnClickListener{
    onProfileClick.onProfileClikc(position)
}

        holder.binding.share.setOnClickListener{
            onProfileClick.onProfileShare(position)
        }


        }

    class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root){

        fun setVideoData(videoModel: NewAppendItItems){

//            binding.tvTitle.text = videoModel.videoTitle
            binding.tvDesc.text = videoModel.videoTitle
            binding.videoView.setVideoPath(videoModel.videoUrl)
            binding.videoView.setOnPreparedListener { mp ->
                binding.progressBar.visibility = View.GONE
                mp.start()
                val videoRatio = mp.videoWidth.toFloat() / mp.videoHeight.toFloat()
                val screenRatio =
                    binding.videoView.width.toFloat() / binding.videoView.height.toFloat()

//                val scale = videoRatio / screenRatio
//                if (scale > 1f) {
//                    binding.videoView.scaleX = scale
//                } else {
//                    binding.videoView.scaleY = (1f / scale)
//                }
            }

            binding.videoView.setOnCompletionListener { MediaPlayer.OnCompletionListener { mp -> mp.start() } }

        }

    }
}