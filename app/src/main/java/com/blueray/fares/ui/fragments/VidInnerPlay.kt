package com.blueray.fares.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.blueray.fares.R
import com.blueray.fares.adapters.VideoAdapter
import com.blueray.fares.adapters.VideoFeedAdapter
import com.blueray.fares.api.OnProfileClick
import com.blueray.fares.api.VideoPlaybackControl
import com.blueray.fares.databinding.ActivityMainBinding
import com.blueray.fares.databinding.OneVidoShowBinding
import com.blueray.fares.databinding.OnevidfragBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.NewAppendItItems
import com.blueray.fares.model.VideoFile
import com.blueray.fares.ui.activities.MainActivity
import com.blueray.fares.ui.viewModels.AppViewModel

class VidInnerPlay : AppCompatActivity(), VideoPlaybackControl {
    private lateinit var navController: NavController






        private lateinit var binding: OneVidoShowBinding
        var arrVideoModel = ArrayList<NewAppendItItems>()
         var newArrVideoModel = mutableListOf<NewAppendItItems>()


        var videoAdapter: VideoFeedAdapter? = null
        private val mainViewModel by viewModels<AppViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = OneVidoShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mSnapHelper: SnapHelper = PagerSnapHelper()
        mSnapHelper.attachToRecyclerView(binding.vidRec)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                if (shouldInterceptBackPress()) {
                    // Your code to handle the back press
                    pauseAllVideos()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
                binding.vidRec.adapter = null
            }


        }
        onBackPressedDispatcher.addCallback(this, callback)

        binding.includeTap.profileBtn.setOnClickListener {
            navController.navigate(R.id.yourChannelFragment)


//
        }
//        val itemTouchHelperCallback = object :
//            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false // We are not interested in move events for this example.
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                // Handle the swipe event, navigate to new fragment
//                val position = viewHolder.adapterPosition // Get swiped item position
//                navController.navigate(R.id.partitionChannelFragment)
//            }
//        }
//
//        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
//
//        itemTouchHelper.attachToRecyclerView(binding.vidRec)


         newArrVideoModel = intent.getSerializableExtra("dataList") as ArrayList<NewAppendItItems>
        val position = intent.getIntExtra("position", 0)

        var isUser = 1
        if (HelperUtils.getUid(this) == "0") {
            isUser = 0
        }
        Log.d("ERTYUIO",newArrVideoModel.size.toString())
        videoAdapter = VideoFeedAdapter(newArrVideoModel, object : OnProfileClick {


            override fun onProfileClikc(pos: Int) {
                Log.d("TEEEESSSSTTT11", newArrVideoModel[pos].videoUrl)

            }

            override fun onProfileShare(pos: Int) {
                // Implement sharing functionality
            }

            override fun onMyProfileClikc() {
                if (HelperUtils.getUid(this@VidInnerPlay) == "0"){
//                    Toast.makeText(context,"يجب تسجيل الدخول", Toast.LENGTH_LONG).show()

                    startActivity(Intent(this@VidInnerPlay, MainActivity::class.java))

                }else {
                    navController.navigate(R.id.yourChannelFragment)
                }
            }

            override fun onmenuClick() {
                TODO("Not yet implemented")
            }
        },this,this,isUser)

        binding.vidRec.adapter = videoAdapter

        // Set up your RecyclerView layout manager
        binding.vidRec.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        binding.vidRec.scrollToPosition(position)






    }
    override fun onPause() {
        super.onPause()
        pauseAllVideos()
//        finish()
    }

    private fun shouldInterceptBackPress(): Boolean {
        // Implement your condition to determine whether to intercept the back press
        // Return true to consume the event, false to let the default behavior proceed
        return false // Replace with your condition
    }
    override fun pauseAllVideos() {
        for (i in 0 until binding.vidRec.childCount) {
            val viewHolder = binding.vidRec.findViewHolderForAdapterPosition(i) as? VideoFeedAdapter.VideoViewHolder
            viewHolder?.player?.pause()
        }
    }

    override fun playVideoAtPosition(position: Int) {
        val viewHolder = binding.vidRec.findViewHolderForAdapterPosition(position) as? VideoFeedAdapter.VideoViewHolder
        viewHolder?.player?.play()
    }


    override fun onBackPressed() {
        pauseAllVideos()

        super.onBackPressed()
    }


}

