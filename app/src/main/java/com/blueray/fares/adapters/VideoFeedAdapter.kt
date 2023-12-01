package com.blueray.fares.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.fares.R
import com.blueray.fares.api.OnProfileClick
import com.blueray.fares.api.VideoPlaybackControl
import com.blueray.fares.databinding.ItemVideoBinding
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.NewAppendItItems
import com.blueray.fares.ui.activities.Profile
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player


class VideoFeedAdapter(val videoUrls: List<NewAppendItItems>, var onProfileClick: OnProfileClick, var context: Context,    private val videoPlaybackControl: VideoPlaybackControl
,var isUser:Int) : RecyclerView.Adapter<VideoFeedAdapter.VideoViewHolder>() {

    class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        var player: ExoPlayer? = null

        fun bind(videoUrl: String) {

            // Initialize player
            if (player == null) {
                player = ExoPlayer.Builder(binding.root.context).build().also {
                    it.repeatMode = Player.REPEAT_MODE_ONE // Set repeat mode for looping
                    it.addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            // Update UI based on playback state
                            if (playbackState == Player.STATE_BUFFERING) {
                                binding.progressBar.show()
                                binding.placeHolderImg.show()
                            } else {
                                binding.progressBar.hide()
                                binding.placeHolderImg.hide()
                            }
                        }
                    })
                }
                val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                player?.setMediaItem(mediaItem)
                player?.prepare()
            }

            // Set click listener to toggle play/pause
            binding.videoView.setOnClickListener {
                player?.let { exoPlayer ->
                    if (exoPlayer.isPlaying) {
                        exoPlayer.pause()
                    } else {
                        exoPlayer.play()
                    }
                }
            }

            binding.videoView.player = player
//            player?.playWhenReady = true
        }

        fun releasePlayer() {
            player?.release()
            player = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return VideoViewHolder(binding)
    }




    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videoUrls[position].videoUrl)
        holder.binding.username.text =  videoUrls[position].userName
        holder.binding.descirption.text =  videoUrls[position].videoTitle


        if (isUser == 1){
            holder.binding.profile.show()
            holder.binding.loginitems.hide()
        }else {
            holder.binding.profile.hide()
            holder.binding.loginitems.show()
        }
        holder.binding.profile.setOnClickListener {
            context.startActivity(Intent(context,Profile::class.java))
        }

        holder.binding.placeHolderImg.setOnClickListener{
            onProfileClick.onProfileClikc(pos = position)
        }
        holder.binding.share.setOnClickListener{
            onProfileClick.onProfileShare(pos = position)
        }
        holder.binding.profile.setOnClickListener {
            onProfileClick.onMyProfileClikc()
        }

        holder.binding.loginitems.setOnClickListener {
            onProfileClick.onMyProfileClikc()
        }


        holder.binding.menu.setOnClickListener {
            onProfileClick.onmenuClick()
        }
        Glide.with(context).load(videoUrls[position].userPic).into(holder.binding.profiel)

        holder.binding.videoView.setOnClickListener {
            if (holder.player?.isPlaying == true) {
                holder.player?.pause()
                holder.binding.placeHolderImg.show()

            } else {
                videoPlaybackControl.pauseAllVideos()
                holder.player?.play()
                holder.binding.placeHolderImg.hide()

            }
        }
    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        holder.player?.pause()
    }
    override fun onViewAttachedToWindow(holder: VideoViewHolder) {
        super.onViewAttachedToWindow(holder)
        // Resume playback when the view is visible
        holder.player?.playWhenReady = true
    }
    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        // Pause playback when the view is not visible
        holder.player?.playWhenReady = false
    }

    override fun getItemCount(): Int = videoUrls.size
}
