package com.blueray.fares.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.blueray.fares.R
import com.blueray.fares.adapters.VideoFeedAdapter
import com.blueray.fares.api.OnProfileClick
import com.blueray.fares.api.VideoPlaybackControl
import com.blueray.fares.databinding.OneVidoShowBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.NewAppendItItems
import com.blueray.fares.ui.activities.MainActivity
import com.blueray.fares.ui.activities.MainView
import com.blueray.fares.ui.activities.SplashScreen
import com.blueray.fares.ui.viewModels.AppViewModel

class VidInnerPlay : AppCompatActivity(), VideoPlaybackControl {
    private lateinit var navController: NavController



    var isAuthintcted = false



        private lateinit var binding: OneVidoShowBinding
        var arrVideoModel = ArrayList<NewAppendItItems>()
         var newArrVideoModel = ArrayList<NewAppendItItems>()


        var videoAdapter: VideoFeedAdapter? = null
        private val mainViewModel by viewModels<AppViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = OneVidoShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mSnapHelper: SnapHelper = PagerSnapHelper()
        mSnapHelper.attachToRecyclerView(binding.vidRec)


        getUserAction()
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

        isAuthintcted = HelperUtils.getUid(this) != "0"

         newArrVideoModel = PartitionChannelFragment.DataHolder.itemsList!!
        val position = intent.getIntExtra("position", 0)
        binding.vidRec.scrollToPosition(position)

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



                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, newArrVideoModel[pos].videoUrl)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)

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
            }

            override fun onProfileLike(pos: Int) {
                if (!isAuthintcted) {
                    shouldUserOut()
                } else {
//                    binding.vidRec.adapter?.notifyItemRemoved(pos)
                    mainViewModel.retriveSetAction(pos.toString(), "node", "like")
                }
            }

            override fun onProfileCommint(pos: Int) {
            }

            override fun onProfileSaved(pos: Int) {
//                binding.vidRec.adapter?.notifyItemRemoved(pos)

                mainViewModel.retriveSetAction(pos.toString(), "node", "save")
            }
        },this,this,isUser)

        binding.vidRec.adapter = videoAdapter

        // Set up your RecyclerView layout manager
        binding.vidRec.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )







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
    fun shouldUserOut() {
        Toast.makeText(this, "يجب تسجيل الدخول", Toast.LENGTH_LONG).show()

        startActivity(Intent(this, SplashScreen::class.java))

    }
    private fun getUserAction() {

        mainViewModel.getSetAction().observe(this) { result ->

            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        Toast.makeText(
                            this,
                            result.data.msg.toString(),
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        Toast.makeText(
                            this,
                            result.data.msg.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }


                }

                is NetworkResults.Error -> {
                    Toast.makeText(
                        this,
                        result.exception.printStackTrace().toString(),
                        Toast.LENGTH_LONG
                    ).show()

                    result.exception.printStackTrace()
                }

                else -> {}
            }
        }
    }


}

