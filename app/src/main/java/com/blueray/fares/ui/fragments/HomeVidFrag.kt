    package com.blueray.fares.ui.fragments
    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.activity.OnBackPressedCallback
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.app.AppCompatDelegate
    import androidx.core.app.ActivityCompat.finishAffinity
    import androidx.core.content.ContextCompat
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.viewModels
    import androidx.navigation.NavController
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.ItemTouchHelper
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.PagerSnapHelper
    import androidx.recyclerview.widget.RecyclerView
    import androidx.recyclerview.widget.SnapHelper
    import com.blueray.fares.R
    import com.blueray.fares.adapters.VideoFeedAdapter
    import com.blueray.fares.api.OnProfileClick
    import com.blueray.fares.api.VideoPlaybackControl
    import com.blueray.fares.databinding.CommentLayoutBinding
    import com.blueray.fares.databinding.OnevidfragBinding
    import com.blueray.fares.helpers.HelperUtils
    import com.blueray.fares.helpers.ViewUtils.hide
    import com.blueray.fares.helpers.ViewUtils.show
    import com.blueray.fares.model.NetworkResults
    import com.blueray.fares.model.NewAppendItItems
    import com.blueray.fares.model.VideoResponse
    import com.blueray.fares.ui.activities.MainView
    import com.blueray.fares.ui.activities.SplashScreen
    import com.blueray.fares.ui.viewModels.AppViewModel
    import com.google.android.material.bottomsheet.BottomSheetDialog
    import com.sendbird.live.SendbirdLive
    import java.text.SimpleDateFormat
    import java.util.Date
    import java.util.Locale


    class HomeVidFrag : Fragment() , VideoPlaybackControl {
        private lateinit var navController: NavController
        var isAuthintcted = false

        private var isLoading = false
        private var noMoreData = false
//        private fun isRootFragment(): Boolean {
//            return navController.backQueue.size == 1
//        }

        private var currentPage = 0
        private val pageSize = 3 // Set this based on your API's page size


        private lateinit var binding: OnevidfragBinding
        private val newArrVideoModel = ArrayList<NewAppendItItems>()
        private var mainArrVideoModel = mutableListOf<VideoResponse>()

        private var lastPlayedPosition = RecyclerView.NO_POSITION
//        private var isDataLoaded = false

        private lateinit var dialog: BottomSheetDialog


        var videoAdapter: VideoFeedAdapter? = null
        private val mainViewModel by viewModels<AppViewModel>()


        fun onHiddenInPager() {
            pauseAllVideos() // Pause video or any action when the fragment is not visible
        }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            mainViewModel.retriveMainVideos(  currentPage,  3,   "1")

            navController = findNavController()
            dialog = BottomSheetDialog(requireActivity())

Log.d("dhfrgjkl",HelperUtils.getUid(requireContext()))

            isAuthintcted = HelperUtils.getUid(requireContext()) != "0"

            binding.includeTap.profile.setOnClickListener {

                if (HelperUtils.getUid(requireContext()) == "0") {
                    Toast.makeText(context, "يجب تسجيل الدخول", Toast.LENGTH_LONG).show()

                    startActivity(Intent(requireContext(), SplashScreen::class.java))

                } else {
                    pauseAllVideos()
                    binding.vidRec.adapter = null

                    navController.navigate(R.id.yourChannelFragment)



                }


                //
            }


//            requireActivity().onBackPressedDispatcher.addCallback(
//                viewLifecycleOwner,
//                object : OnBackPressedCallback(true) {
//                    override fun handleOnBackPressed() {
//                        // Check if the current destination is the start destination
//                        if (navController.currentDestination?.id == navController.graph.startDestinationId) {
//                            // If this is the start destination, finish the activity to exit the app
//                            requireActivity().finish()
//                        } else {
//                            // If this is not the start destination, let the system handle the back press
//                            isEnabled = false
//                            requireActivity().onBackPressed()
//                        }
//                    }
//                })


            setupRecycler()

            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        // Custom action to be performed when back is pressed
                        pauseAllVideos()

                        AlertDialog.Builder(requireContext())
                            .setTitle("خروج")
                            .setMessage("هل انت متاكد من الخروج؟")
                            .setPositiveButton("نعم") { _, _ ->
                                finishAffinity(requireActivity())

                            }
                            .setNegativeButton("لا", null)
                            .show()

                    }
                })

            getVideosView()
            getUserAction()
            if (!isLoading) {
                mainViewModel.retriveMainVideos(  currentPage  + 1,  3,   "1")
                Log.d("HOOOOMEEEEE",currentPage.toString())

            }

        }


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {

            binding = OnevidfragBinding.inflate(layoutInflater)

            //        arrVideoModel.add(NewAppendItItems("Tree with flowers","The branches of a tree wave in the breeze, with pointy leaves ","https://assets.mixkit.co/videos/preview/mixkit-tree-with-yellow-flowers-1173-large.mp4"))
            //        arrVideoModel.add(NewAppendItItems("multicolored lights","A man with a small beard and mustache wearing a white sweater, sunglasses, and a backwards black baseball cap turns his head in different directions under changing colored lights.","https://assets.mixkit.co/videos/preview/mixkit-man-under-multicolored-lights-1237-large.mp4"))
            //        arrVideoModel.add(NewAppendItItems("holding neon light","Bald man with a short beard wearing a large jean jacket holds a long tubular neon light thatch","https://assets.mixkit.co/videos/preview/mixkit-man-holding-neon-light-1238-large.mp4"))
            //        arrVideoModel.add(NewAppendItItems("Sun over hills","The sun sets or rises over hills, a body of water beneath them.","https://assets.mixkit.co/videos/preview/mixkit-sun-over-hills-1183-large.mp4"))



//
            val itemTouchHelperCallback =
                object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false // Not interested in move events.
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition // Get the swiped item's position
                        val swipedItem = newArrVideoModel[position]

                        // Check user login status
//                    if (HelperUtils.getUid(requireContext()) == "0") {
//                        Toast.makeText(context,"يجب تسجيل الدخول",Toast.LENGTH_LONG).show()
//                        startActivity(Intent(requireContext(), MainActivity::class.java))
//                    } else {
                        // Example: Sending ID of the swiped item


                        pauseAllVideos()
                        val bundle = Bundle().apply {
//                            if (swipedItem.type == "poet") {

                                putString(
                                    "usernName",
                                    swipedItem.userName
                                ) // Use your item's unique identifier
                                putString(
                                    "userIdes",
                                    swipedItem.userId
                                ) // Use your item's unique identifier
                                putString(
                                    "userImg",
                                    swipedItem.userPic
                                ) // Use your item's unique identifier
                                putString(
                                    "fullname",
                                    swipedItem.firstName + swipedItem.lastName
                                ) // Use your item's unique identifier

                                putString(
                                    "numOfFollowers",
                                    swipedItem.numOfFollowers.toString()
                                ) // Use your item's unique identifier

                                putString(
                                    "numOfFollowing",
                                    swipedItem.numOfFollowing.toString()
                                ) // Use your item's unique identifier


                                putString(
                                    "numOfLikes",
                                    swipedItem.numOfLikes.toString()
                                ) // Use your item's unique identifier


                            putString(
                                "isUserFollower",
                                swipedItem.target_user?.target_user_follow_flag.toString()
                            ) // Use your item's unique identifier

Log.d("FlageVV",swipedItem.target_user?.target_user_follow_flag.toString())

//                            }

//                            else {
//                                putString(
//                                    "usernName",
//                                    swipedItem.userName
//                                ) // Use your item's unique identifier
//                                putString(
//                                    "userIdes",
//                                    swipedItem.userId
//                                ) // Use your item's unique identifier
//                                putString(
//                                    "userImg",
//                                    swipedItem.userPic
//                                ) // Use your item's unique identifier
//                                putString(
//                                    "fullname",
//                                    swipedItem.bandNam
//                                ) // Use your item's unique identifier
//
//                            }


                        }
                        pauseAllVideos()
                        binding.vidRec.adapter = null

                        if (HelperUtils.getUid(requireContext()) != swipedItem.userId){
                            navController.navigate(R.id.partitionChannelFragment, bundle)

                        }else {
                            navController.navigate(R.id.myAccountFragment, bundle)

                        }

//                    }
                    }
                }

            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(binding.vidRec)


                                        val mSnapHelper: SnapHelper = PagerSnapHelper()
                            mSnapHelper.attachToRecyclerView(binding.vidRec)

            //
            //        videoAdapter = VideoAdapter(arrVideoModel,object :OnProfileClick{
            //            override fun onProfileClikc(pos: Int) {
            //             navController.navigate(R.id.yourChannelFragment)
            //
            //            }
            //
            //            override fun onProfileShare(pos: Int) {
            //                val shareIntent = Intent(Intent.ACTION_SEND)
            //                shareIntent.type = "text/plain"
            //                shareIntent.putExtra(Intent.EXTRA_TEXT, arrVideoModel[pos].videoUrl)
            //
            //                startActivity(Intent.createChooser(shareIntent, "مبادرة شاعر الرؤيا"))
            //
            //
            //
            //            }
            //        })


            //        getNewItems()


            return binding.root


        }

        fun getVideosView() {
            mainViewModel.getMainVideos().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is NetworkResults.Success -> {
                        binding.img.hide()

//                        result.data.f
                        //                    append new Items
                        isLoading = false
                        if (result.data.datass.isEmpty()) {
                            noMoreData = true
                            binding.progg.hide()

                        } else {
                            binding.progg.hide()
                            val startPosition = newArrVideoModel.size
                            // Iterate over each item and add to newArrVideoModel
                            mainArrVideoModel = result.data.datass.toMutableList()
                            val sortedList = result.data.datass.sortedBy { it.created.toDate() } // Assuming 'created' is a valid date string

                            sortedList.forEach { item ->
                                var vidLink = ""
                                val adaptiveFile = item.vimeo_detials?.files?.firstOrNull {
                                    it.rendition == "adaptive" || it.rendition == "360p" || it.rendition =="240p"|| it.rendition =="540p" || it.rendition =="720p" ||it.rendition =="1080p"
                                }
                                vidLink = adaptiveFile?.link ?: "https://firebasestorage.googleapis.com/v0/b/kenz-e9a7c.appspot.com/o/1024907363-preview.mp4?alt=media&token=a720feff-f094-4e5e-85fe-fca5e379d5d8"




                                newArrVideoModel.add(
                                    NewAppendItItems(
                                        item.title,
                                        item.id.toString(),
                                        item.created,
                                        vidLink,
                                        item.auther.uid,
                                        item.auther.username,
                                        item.vimeo_detials.duration,
                                        firstName = item.auther.profile_data.first_name,
                                        lastName = item.auther.profile_data.last_name,
                                        type = item.auther.type,
                                        bandNam = item.auther.profile_data.band_name,
                                        userPic = item.auther.profile_data.user_picture,
                                        userFav = item.video_actions_per_user.favorites.toString(),
                                        userSave = item.video_actions_per_user.save.toString(),
                                        target_user = result.data.target_user,
                                        video_counts =  item.video_counts,
                                        numOfFollowers = item.auther.numOfFollowers,
                                        numOfFollowing = item.auther.numOfFollowing,
                                        numOfLikes = item.auther.numOfLikes,

                                                nodeId = item.id,

                                    )
                                )
                            }


//                            if (isLoading == false){
////                                videoAdapter?.notifyItemRangeInserted(
////                                    startPosition,
////                                    result.data.datass.size
////                                )
//                            }
//                            val mSnapHelper: SnapHelper = PagerSnapHelper()
//                            mSnapHelper.attachToRecyclerView(binding.vidRec)

                            videoAdapter?.notifyItemRangeInserted(startPosition, result.data.datass.size)


                        }


                    }

                    is NetworkResults.Error -> {
                        binding.img.show()

                        Log.d("ERRRRor", result.exception.toString())
                    }

                    is NetworkResults.NoInternet -> TODO()
                }
            }

        }

        fun setupRecycler() {
            var isUser = 1
            if (HelperUtils.getUid(requireContext()) == "0") {
                isUser = 0
            }
            Log.d("ERTYUIO", newArrVideoModel.size.toString())
            videoAdapter = VideoFeedAdapter(newArrVideoModel, object : OnProfileClick {


                override fun onProfileClikc(pos: Int) {
                    val swipedItem = newArrVideoModel[pos]
                    Log.d("TEEEESSSSTTT11", newArrVideoModel[pos].videoUrl)
                    // Example: Sending ID of the swiped item
                    val bundle = Bundle().apply {
//                        if (swipedItem.type == "poet") {

                            putString(
                                "usernName",
                                swipedItem.userName
                            ) // Use your item's unique identifier
                            putString(
                                "userIdes",
                                swipedItem.userId
                            ) // Use your item's unique identifier
                            putString(
                                "userImg",
                                swipedItem.userPic
                            ) // Use your item's unique identifier
                            putString(
                                "fullname",
                                swipedItem.firstName + swipedItem.lastName
                            ) // Use your item's unique identifier



                        putString(
                            "numOfFollowers",
                            swipedItem.numOfFollowers.toString()
                        ) // Use your item's unique identifier

                        putString(
                            "numOfFollowing",
                            swipedItem.numOfFollowing.toString()
                        ) // Use your item's unique identifier


                        putString(
                            "numOfLikes",
                            swipedItem.numOfLikes.toString()
                        ) // Use your item's unique identifier


                        putString(
                            "isUserFollower",
                            swipedItem.target_user?.target_user_follow_flag.toString()
                        ) // Use your item's unique identifier


                        putString(
                            "userIdes",
                            swipedItem.userId
                        ) // Use your item's unique identifier
                        Log.d("werstyuio",swipedItem.target_user?.target_user_follow_flag.toString())


//                        } else {
//                            putString(
//                                "usernName",
//                                swipedItem.userName
//                            ) // Use your item's unique identifier
//                            putString(
//                                "userIdes",
//                                swipedItem.userId
//                            ) // Use your item's unique identifier
//                            putString(
//                                "userImg",
//                                swipedItem.userPic
//                            ) // Use your item's unique identifier
//                            putString(
//                                "fullname",
//                                swipedItem.bandNam
//                            ) // Use your item's unique identifier
//
//                        }


                    }
                    pauseAllVideos()
                    binding.vidRec.adapter = null
                    if (HelperUtils.getUid(requireContext()) != swipedItem.userId){
                        navController.navigate(R.id.partitionChannelFragment, bundle)

                    }else {
                        navController.navigate(R.id.myAccountFragment, bundle)

                    }


                }

                override fun onProfileShare(pos: Int) {
                    // Implement sharing functionality


//
//                    val sendIntent: Intent = Intent().apply {
//                        action = Intent.ACTION_SEND
//                        putExtra(Intent.EXTRA_TEXT, newArrVideoModel[pos].videoUrl)
//                        type = "text/plain"
//                    }
//
//                    val shareIntent = Intent.createChooser(sendIntent, null)
//                    startActivity(shareIntent)


                }


                override fun onMyProfileClikc() {

                    if (HelperUtils.getUid(requireContext()) == "0") {
                        Toast.makeText(context, "يجب تسجيل الدخول", Toast.LENGTH_LONG).show()

                        startActivity(Intent(requireContext(), SplashScreen::class.java))

                    } else {
                        navController.navigate(R.id.yourChannelFragment)
                    }

                }

                override fun onmenuClick() {
//                    menus
                    showBottomSheet()
                }

                override fun onProfileLike(pos: Int) {
                    if (!isAuthintcted) {
                        shouldUserOut()
                    } else {
                        mainViewModel.retriveSetAction(pos.toString(), "node", "like")
                    }
                }

                override fun onProfileCommint(pos: Int) {
                    if (!isAuthintcted) {
                        shouldUserOut()
                    } else {
//                        todo DisplayCommints
//                        mainViewModel.retriveSetAction(mainArrVideoModel[pos].id,"comment","like")
                    }
                }

                override fun onProfileSaved(pos: Int) {
                    if (!isAuthintcted) {
                        shouldUserOut()
                    } else {
                        mainViewModel.retriveSetAction(pos.toString(), "node", "save")
                    }
                }

                override fun onProfileDeletVideo(pos: Int) {

                }
            }, requireContext(), this, isUser)


            // Set up your RecyclerView layout manager
            binding.vidRec.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            binding.vidRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    if (!isLoading && !noMoreData && totalItemCount <= (lastVisibleItem + 12)) {
                        loadMoreItems()
                    }
                }
            })


            binding.vidRec.adapter = videoAdapter
        }

        private fun loadMoreItems() {
            if (!noMoreData && !isLoading) {
                isLoading = true
                currentPage++
                binding.progg.show()
                mainViewModel.retriveMainVideos(page = currentPage, pageLimit = pageSize, ishome = "1")
            }
        }



        private fun getUserAction() {

            mainViewModel.getSetAction().observe(viewLifecycleOwner) { result ->

                when (result) {
                    is NetworkResults.Success -> {
                        if (result.data.status == 200) {
                            Toast.makeText(
                                requireContext(),
                                result.data.msg.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        } else {
                            Toast.makeText(
                                requireContext(),
                                result.data.msg.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    }

                    is NetworkResults.Error -> {
                        Toast.makeText(
                            requireContext(),
                            result.exception.printStackTrace().toString(),
                            Toast.LENGTH_LONG
                        ).show()

                        result.exception.printStackTrace()
                    }

                    else -> {}
                }
            }
        }


        override fun onPause() {
            super.onPause()
//            pauseAllVideos()
//            videoAdapter?.notifyDataSetChanged() // Or use more specific notify methods for better performance
//videoAdapter = null
//            pauseAllVideos()
//            binding.vidRec.adapter = null
//            mainViewModel.retriveMainVideos(page = 0, pageLimit = 3, ishome = "1")

pauseAllVideos()

            for (i in 0 until binding.vidRec.childCount) {
                val viewHolder0 = binding.vidRec.findViewHolderForAdapterPosition(i) as? VideoFeedAdapter.VideoViewHolder

                val viewHolder = binding.vidRec.findViewHolderForAdapterPosition(i + 1) as? VideoFeedAdapter.VideoViewHolder
                val viewHolder2 = binding.vidRec.findViewHolderForAdapterPosition(i + 2) as? VideoFeedAdapter.VideoViewHolder
                val viewHolder3= binding.vidRec.findViewHolderForAdapterPosition(i + 3) as? VideoFeedAdapter.VideoViewHolder
                val viewHolder4 = binding.vidRec.findViewHolderForAdapterPosition(i + 4) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde5 = binding.vidRec.findViewHolderForAdapterPosition(i + 5) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde6 = binding.vidRec.findViewHolderForAdapterPosition(i +6) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde7 = binding.vidRec.findViewHolderForAdapterPosition(i + 7) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde8 = binding.vidRec.findViewHolderForAdapterPosition(i + 8) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde9 = binding.vidRec.findViewHolderForAdapterPosition(i + 9) as? VideoFeedAdapter.VideoViewHolder

                val viewHolde10 = binding.vidRec.findViewHolderForAdapterPosition(i + 10) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde11 = binding.vidRec.findViewHolderForAdapterPosition(i + 11) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde12 = binding.vidRec.findViewHolderForAdapterPosition(i + 12) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde13 = binding.vidRec.findViewHolderForAdapterPosition(i + 13) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde14 = binding.vidRec.findViewHolderForAdapterPosition(i + 14) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde15 = binding.vidRec.findViewHolderForAdapterPosition(i + 15) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde16 = binding.vidRec.findViewHolderForAdapterPosition(i + 16) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde17 = binding.vidRec.findViewHolderForAdapterPosition(i + 17) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde18 = binding.vidRec.findViewHolderForAdapterPosition(i + 18) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde19 = binding.vidRec.findViewHolderForAdapterPosition(i + 19) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde20 = binding.vidRec.findViewHolderForAdapterPosition(i + 20) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde21 = binding.vidRec.findViewHolderForAdapterPosition(i + 21) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde22 = binding.vidRec.findViewHolderForAdapterPosition(i + 22) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde23 = binding.vidRec.findViewHolderForAdapterPosition(i + 23) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde24 = binding.vidRec.findViewHolderForAdapterPosition(i + 24) as? VideoFeedAdapter.VideoViewHolder

                val viewHolde25 = binding.vidRec.findViewHolderForAdapterPosition(i + 25) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde26 = binding.vidRec.findViewHolderForAdapterPosition(i + 26) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde27 = binding.vidRec.findViewHolderForAdapterPosition(i + 27) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde28 = binding.vidRec.findViewHolderForAdapterPosition(i + 28) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde29 = binding.vidRec.findViewHolderForAdapterPosition(i + 29) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde30 = binding.vidRec.findViewHolderForAdapterPosition(i + 30) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde31 = binding.vidRec.findViewHolderForAdapterPosition(i + 31) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde32 = binding.vidRec.findViewHolderForAdapterPosition(i + 32) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde33 = binding.vidRec.findViewHolderForAdapterPosition(i + 33) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde34 = binding.vidRec.findViewHolderForAdapterPosition(i + 34) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde35 = binding.vidRec.findViewHolderForAdapterPosition(i + 35) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde36 = binding.vidRec.findViewHolderForAdapterPosition(i + 36) as? VideoFeedAdapter.VideoViewHolder

                val viewHolde37 = binding.vidRec.findViewHolderForAdapterPosition(i + 37) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde38 = binding.vidRec.findViewHolderForAdapterPosition(i + 38) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde39 = binding.vidRec.findViewHolderForAdapterPosition(i + 39) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde40 = binding.vidRec.findViewHolderForAdapterPosition(i + 40) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde41 = binding.vidRec.findViewHolderForAdapterPosition(i + 41) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde42 = binding.vidRec.findViewHolderForAdapterPosition(i + 42) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde43 = binding.vidRec.findViewHolderForAdapterPosition(i + 43) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde44 = binding.vidRec.findViewHolderForAdapterPosition(i + 44) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde45 = binding.vidRec.findViewHolderForAdapterPosition(i + 45) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde46 = binding.vidRec.findViewHolderForAdapterPosition(i + 46) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde47 = binding.vidRec.findViewHolderForAdapterPosition(i + 47) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde48 = binding.vidRec.findViewHolderForAdapterPosition(i + 48) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde49 = binding.vidRec.findViewHolderForAdapterPosition(i + 49) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde50 = binding.vidRec.findViewHolderForAdapterPosition(i + 50) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde51 = binding.vidRec.findViewHolderForAdapterPosition(i + 51) as? VideoFeedAdapter.VideoViewHolder


                viewHolder?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolder2?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolder3?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolder4?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde5?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde6?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde7?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde8?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde9?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolder0?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde10?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde11?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde12?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde13?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde14?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde15?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde16?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde17?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde18?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde19?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde20?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde21?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde22?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde21?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde22?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde23?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde24?.player?.pause() // Replace 'play()' with the appropriate method to resume video


                viewHolde25?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde26?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde27?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde28?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde29?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde30?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde31?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde32?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde33?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde34?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde35?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde36?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde37?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde38?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde39?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde40?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde41?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde42?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde43?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde44?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde45?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde46?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde47?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde48?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde49?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde50?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde51?.player?.pause() // Replace 'play()' with the appropriate method to resume video


            }

        }

        fun resumeAllVideos() {
            for (i in 0 until binding.vidRec.childCount) {
                val viewHolder = binding.vidRec.findViewHolderForAdapterPosition(i + 1) as? VideoFeedAdapter.VideoViewHolder
                val viewHolder2 = binding.vidRec.findViewHolderForAdapterPosition(i + 2) as? VideoFeedAdapter.VideoViewHolder
                val viewHolder3= binding.vidRec.findViewHolderForAdapterPosition(i + 3) as? VideoFeedAdapter.VideoViewHolder
                val viewHolder4 = binding.vidRec.findViewHolderForAdapterPosition(i + 4) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde5 = binding.vidRec.findViewHolderForAdapterPosition(i + 5) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde6 = binding.vidRec.findViewHolderForAdapterPosition(i +6) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde7 = binding.vidRec.findViewHolderForAdapterPosition(i + 7) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde8 = binding.vidRec.findViewHolderForAdapterPosition(i + 8) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde9 = binding.vidRec.findViewHolderForAdapterPosition(i + 9) as? VideoFeedAdapter.VideoViewHolder

                viewHolder?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolder2?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolder3?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolder4?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde5?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde6?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde7?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde8?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde9?.player?.pause() // Replace 'play()' with the appropriate method to resume video

            }
        }


        override fun onResume() {
            super.onResume()
//            binding.vidRec.show()
//            pauseAllVideos()
//            binding.vidRec.adapter = null
//            pauseAllVideos()


        }

        override fun onStop() {
            super.onStop()
            pauseAllVideos()



        }

        override fun onStart() {
            super.onStart()
            Log.d("HOOOOMEEEEE",currentPage.toString())

        }

        override fun pauseAllVideos() {
            for (i in 0 until binding.vidRec.childCount) {
                val viewHolder0 = binding.vidRec.findViewHolderForAdapterPosition(i) as? VideoFeedAdapter.VideoViewHolder

                val viewHolder = binding.vidRec.findViewHolderForAdapterPosition(i + 1) as? VideoFeedAdapter.VideoViewHolder
                val viewHolder2 = binding.vidRec.findViewHolderForAdapterPosition(i + 2) as? VideoFeedAdapter.VideoViewHolder
                val viewHolder3= binding.vidRec.findViewHolderForAdapterPosition(i + 3) as? VideoFeedAdapter.VideoViewHolder
                val viewHolder4 = binding.vidRec.findViewHolderForAdapterPosition(i + 4) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde5 = binding.vidRec.findViewHolderForAdapterPosition(i + 5) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde6 = binding.vidRec.findViewHolderForAdapterPosition(i +6) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde7 = binding.vidRec.findViewHolderForAdapterPosition(i + 7) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde8 = binding.vidRec.findViewHolderForAdapterPosition(i + 8) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde9 = binding.vidRec.findViewHolderForAdapterPosition(i + 9) as? VideoFeedAdapter.VideoViewHolder

                val viewHolde10 = binding.vidRec.findViewHolderForAdapterPosition(i + 10) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde11 = binding.vidRec.findViewHolderForAdapterPosition(i + 11) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde12 = binding.vidRec.findViewHolderForAdapterPosition(i + 12) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde13 = binding.vidRec.findViewHolderForAdapterPosition(i + 13) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde14 = binding.vidRec.findViewHolderForAdapterPosition(i + 14) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde15 = binding.vidRec.findViewHolderForAdapterPosition(i + 15) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde16 = binding.vidRec.findViewHolderForAdapterPosition(i + 16) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde17 = binding.vidRec.findViewHolderForAdapterPosition(i + 17) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde18 = binding.vidRec.findViewHolderForAdapterPosition(i + 18) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde19 = binding.vidRec.findViewHolderForAdapterPosition(i + 19) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde20 = binding.vidRec.findViewHolderForAdapterPosition(i + 20) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde21 = binding.vidRec.findViewHolderForAdapterPosition(i + 21) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde22 = binding.vidRec.findViewHolderForAdapterPosition(i + 22) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde23 = binding.vidRec.findViewHolderForAdapterPosition(i + 23) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde24 = binding.vidRec.findViewHolderForAdapterPosition(i + 24) as? VideoFeedAdapter.VideoViewHolder

                val viewHolde25 = binding.vidRec.findViewHolderForAdapterPosition(i + 25) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde26 = binding.vidRec.findViewHolderForAdapterPosition(i + 26) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde27 = binding.vidRec.findViewHolderForAdapterPosition(i + 27) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde28 = binding.vidRec.findViewHolderForAdapterPosition(i + 28) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde29 = binding.vidRec.findViewHolderForAdapterPosition(i + 29) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde30 = binding.vidRec.findViewHolderForAdapterPosition(i + 30) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde31 = binding.vidRec.findViewHolderForAdapterPosition(i + 31) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde32 = binding.vidRec.findViewHolderForAdapterPosition(i + 32) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde33 = binding.vidRec.findViewHolderForAdapterPosition(i + 33) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde34 = binding.vidRec.findViewHolderForAdapterPosition(i + 34) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde35 = binding.vidRec.findViewHolderForAdapterPosition(i + 35) as? VideoFeedAdapter.VideoViewHolder
                val viewHolde36 = binding.vidRec.findViewHolderForAdapterPosition(i + 36) as? VideoFeedAdapter.VideoViewHolder


                viewHolder?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolder2?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolder3?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolder4?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde5?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde6?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde7?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde8?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde9?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolder0?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde10?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde11?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde12?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde13?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde14?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde15?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde16?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde17?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde18?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde19?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde20?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde21?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde22?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde21?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde22?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde23?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde24?.player?.pause() // Replace 'play()' with the appropriate method to resume video


                viewHolde25?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde26?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde27?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde28?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde29?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde30?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde31?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde32?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde33?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde34?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde35?.player?.pause() // Replace 'play()' with the appropriate method to resume video
                viewHolde36?.player?.pause() // Replace 'play()' with the appropriate method to resume video


            }

        }



        override fun playVideoAtPosition(position: Int) {
            val viewHolder =
                binding.vidRec.findViewHolderForAdapterPosition(position) as? VideoFeedAdapter.VideoViewHolder
//            viewHolder?.player?.play()
        }

        //fun getNewItems(){
        //    mainViewModel.getVideoOption().observe(viewLifecycleOwner) { result ->
        //        when (result) {
        //            is NetworkResults.Success -> {
        //
        //                if (result.data.files.isNullOrEmpty()){
        //
        //                }else {
        //
        //                }
        //loadMoreItems
        ////                for (i in videoOptionList){
        ////                }
        //--
        ////                if (isAdded)
        //
        ////                    showDialog(videoOptionList)
        //            }
        //            is NetworkResults.Error -> {
        //                result.exception.printStackTrace()
        //            }
        //
        //            is NetworkResults.NoInternet -> TODO()
        //        }
        //    }
        //}

        fun shouldUserOut() {
            Toast.makeText(context, "يجب تسجيل الدخول", Toast.LENGTH_LONG).show()

            startActivity(Intent(requireContext(), SplashScreen::class.java))

        }

        private fun showBottomSheet() {


            if (!dialog.isShowing) {

                var buyType = 0

                // initialize binding for bottom sheet
                val botBinding = CommentLayoutBinding.inflate(layoutInflater)

                // Set rounded coaaa\rner drawable as background
                botBinding.root.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.buttom_sheet_back)

                // address viewBinding to the bottomSheet dialog
                dialog.setContentView(botBinding.root)

                dialog.show()


            }

        }
        @Deprecated("Deprecated in Java")
        override fun setUserVisibleHint(isVisibleToUser: Boolean) {
            super.setUserVisibleHint(isVisibleToUser)
            if (!isVisibleToUser) {
//                pauseAllVideos()
            } else {
                // Resume or start your video here

            }
        }





        fun String.toDate(format: String = "yyyy-MM-dd"): Date {
            return SimpleDateFormat(format, Locale.getDefault()).parse(this) ?: Date()
        }

    }