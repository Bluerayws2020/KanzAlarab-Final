package com.blueray.fares.adapters

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.arthenica.mobileffmpeg.FFmpeg
import com.blueray.fares.databinding.ActivitiesCountItemsBinding
import com.blueray.fares.databinding.VideoListItemBinding
import com.blueray.fares.model.NewAppendItItems
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class VideoItemAdapter
    (var arrVideo:ArrayList<NewAppendItItems>,
var listener : (id : String )-> Unit,
            context:Context
): RecyclerView.Adapter<VideoItemAdapter.MyViewHolder>()
{

    inner class MyViewHolder(val binding : VideoListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = VideoListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = arrVideo.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val data = list[position]
        val context = holder.itemView.context
        holder.apply {

        }
        holder.itemView.setOnClickListener {
            listener.invoke("")
        }
        val videoItem = arrVideo[position]
        val videoPath = videoItem.videoUrl // The path of the video
        val gifPath = generateGifPath() // Generate a path for the new GIF

        // Use coroutines for background processing
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val startTime = "00:00:05" // for example
                val duration = "10" // for example
                convertVideoToGif(videoPath, gifPath, startTime, duration)
                withContext(Dispatchers.Main) {
                    displayGif(holder.binding.gifs, gifPath)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Log error or show an error message
                    Log.d("VideoAdapter", "Error creating GIF", e)
                    // Optionally, show an error placeholder in the ImageView
                }
            }
        }
    }



    private fun generateGifPath(): String {
        // Define the directory where the GIF will be saved
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        // Create a unique filename, you could use a timestamp or a UUID
        val filename = "gif_${System.currentTimeMillis()}.gif"

        // Create the full path
        val gifPath = File(directory, filename).absolutePath

        return gifPath
    }

    // Function to convert video to GIF (as discussed earlier)

    fun convertVideoToGif(videoPath: String, gifPath: String, startTime: String, duration: String) {
        val cmd = "-ss $startTime -t $duration -i $videoPath -vf fps=10,scale=320:-1:flags=lanczos -c:v gif -f gif $gifPath"

        FFmpeg.executeAsync(cmd) { _, returnCode ->
            if (returnCode == 0) {
                // Success

            } else {
                // Error handling
                Log.d("ERTYUI","ERERERER")
            }
        }
    }


    fun displayGif(imageView: ImageView, gifPath: String) {
        Glide.with(imageView.context)
            .asGif()
            .load(gifPath)
            .into(imageView)
    }
}