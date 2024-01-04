package com.blueray.fares.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueray.fares.R
import com.blueray.fares.api.OnProfileClick
import com.blueray.fares.api.VideoPlaybackControl
import com.blueray.fares.databinding.ItemVideoBinding
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.NewAppendItItems
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player


class VideoFeedAdapter(
     val videoUrls: ArrayList<NewAppendItItems>, var onProfileClick: OnProfileClick, var context: Context, private val videoPlaybackControl: VideoPlaybackControl
    ,
    var isUser:Int) : RecyclerView.Adapter<VideoFeedAdapter.VideoViewHolder>() {
    var likeCount = 0
    var commintCount = 0

    class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        var player: ExoPlayer? = null


        val gestureDetector = GestureDetector(binding.root.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                binding.likeBtn.performClick() // Simulate a click on the like button
                return true
            }
        })

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
                                binding.placeHolderBackground.hide()
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
//                        exoPlayer.pause()
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
        val item = videoUrls[position]


        holder.itemView.setOnTouchListener { _, event ->
            holder.gestureDetector.onTouchEvent(event)
            true
        }
            holder.binding.username.text =  videoUrls[position].userName
            holder.binding.description.text =  videoUrls[position].videoTitle
//
            // Save button
            holder.binding.saveBtn.setOnClickListener {
                item.userSave = if(item.userSave == "1") "0" else "1"
                updateSaveButtonUI(holder, item)
                onProfileClick.onProfileSaved(item.nodeId.toInt())
            }

        if (videoUrls[position].userFav == "1"){
            holder.binding.likeBtn.setImageResource(R.drawable.heartss)

        }else {
            holder.binding.likeBtn.setImageResource(R.drawable.heart)

        }


        holder.binding.deltBtn.setOnClickListener {
            onProfileClick.onProfileDeletVideo(position)
        }
        holder.binding.likeBtn.setOnClickListener {
            val item = videoUrls[position]
            if (item.userFav == "1") {
                item.userFav = "0"
                // Decrease like count if it's greater than 0
                if (item.video_counts?.like_count ?: 0 > 0) {
                    item.video_counts?.like_count = item.video_counts?.like_count?.minus(1)!!
                }
            } else {
                item.userFav = "1"
                // Increase like count
                item.video_counts?.like_count = item.video_counts?.like_count?.plus(1)!!
            }
            updateLikeButtonUI(holder, item)
//            onProfileClick.onProfileLike(pos = item.nodeId.toInt())

        }

            holder.binding.loginitems.hide()

//
//
//        if (isUser == 1){
////            holder.binding.profile.show()
//            holder.binding.loginitems.hide()
//        }else {
////            holder.binding.profile.hide()
//            holder.binding.loginitems.show()
//        }

if (isUser == 3001){

    holder.binding.deltBtn.show()
}else {
    holder.binding.deltBtn.hide()

}
Log.d("LikkkesCountss",videoUrls[position].video_counts?.like_count.toString())
        likeCount = videoUrls[position].video_counts?.like_count!!
        commintCount = videoUrls[position].video_counts?.like_count ?: 0

        holder.binding.commentsCount.text = commintCount.toString()

        holder.binding.likesCount.text = likeCount.toString()

        // Like button logic


        if (videoUrls[position].userSave == "1"){
holder.binding.saveBtn.setImageResource(R.drawable.baseline_bookmark_24)

        }else {
            holder.binding.saveBtn.setImageResource(R.drawable.save)

        }
//




        holder.binding.commentBtn.setOnClickListener{

            onProfileClick.onProfileCommint(pos = position)
        }

        holder.binding.shareBtn.setOnClickListener{
//            onProfileClick.onProfileShare(pos = position)
        }




        holder.binding.loginitems.setOnClickListener {
            onProfileClick.onMyProfileClikc()
        }
        holder.binding.description.setOnClickListener{
            onProfileClick.onProfileClikc(position)
        }

        holder.binding.username.setOnClickListener{
            onProfileClick.onProfileClikc(position)
        }
        holder.binding.profiel.setOnClickListener{
            onProfileClick.onProfileClikc(position)
        }

        holder.binding.shareBtn.setOnClickListener{
            onProfileClick.onProfileShare(position)
        }


        holder.binding.menu.setOnClickListener {
            onProfileClick.onmenuClick()
        }
        Glide.with(context).load(videoUrls[position].userPic).into(holder.binding.profielImage)

        holder.binding.videoView.setOnClickListener {
            if (holder.player?.isPlaying == true) {
                holder.player?.pause()
                holder.binding.placeHolderBackground.show()

            } else {
                videoPlaybackControl.pauseAllVideos()
                holder.player?.play()
                holder.binding.placeHolderBackground.hide()

            }
        }
    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        holder.releasePlayer()
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


    private fun updateLikeButtonUI(holder: VideoViewHolder, item: NewAppendItItems) {
        // Update heart icon based on userFav
        holder.binding.likeBtn.setImageResource(if (item.userFav == "1") R.drawable.heartss else R.drawable.heart)
        // Update like count text
        holder.binding.likesCount.text = item.video_counts?.like_count.toString()
    }
    private fun updateSaveButtonUI(holder: VideoViewHolder, item: NewAppendItItems) {
        holder.binding.saveBtn.setImageResource(if (item.userSave == "1") R.drawable.baseline_bookmark_24 else R.drawable.save)
        holder.binding.commentsCount.text = item.video_counts?.save_count.toString()
    }
    override fun getItemCount(): Int = videoUrls.size

    fun appendData(newItems: MutableList<NewAppendItItems>) {
        val startPosition = videoUrls.size
        videoUrls.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }


}
