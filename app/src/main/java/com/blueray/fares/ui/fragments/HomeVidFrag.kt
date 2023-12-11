    package com.blueray.fares.ui.fragments

    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.activity.OnBackPressedCallback
    import androidx.appcompat.app.AppCompatDelegate
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


    class HomeVidFrag : Fragment() , VideoPlaybackControl {
        private lateinit var navController: NavController
        var isAuthintcted = false

        private var isLoading = false
        private var noMoreData = false

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
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

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

            setupRecycler()

            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        // Custom action to be performed when back is pressed
                        pauseAllVideos()
                    }
                })

            getVideosView()
            getUserAction()
            if (!isLoading) {
                mainViewModel.retriveMainVideos(page = 0, pageLimit = 3, ishome = "1")
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

            val mSnapHelper: SnapHelper = PagerSnapHelper()
            mSnapHelper.attachToRecyclerView(binding.vidRec)


//
            val itemTouchHelperCallback =
                object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
                            result.data.datass.forEach { item ->
                                var vidLink = ""
                                val adaptiveFile = item.vimeo_detials?.files?.firstOrNull {
                                    it.rendition == "adaptive" || it.rendition == "360"
                                }
                                vidLink = adaptiveFile?.link ?: item.file


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
                            videoAdapter?.notifyItemRangeInserted(
                                startPosition,
                                result.data.datass.size
                            )
//                            videoAdapter?.appendData(newArrVideoModel) // newItemsList is the list of new data items


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
                    navController.navigate(R.id.partitionChannelFragment, bundle)
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
            }, requireContext(), this, isUser)


            // Set up your RecyclerView layout manager
            binding.vidRec.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )


            binding.vidRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (noMoreData) return  // Stop pagination if no more data

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()

                    if (!isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                        loadMoreItems()
                        isLoading = true
                    }

                }
            })
            binding.vidRec.adapter = videoAdapter
        }

        private fun loadMoreItems() {
            if (noMoreData) {
                Log.d("No MOREEE DATA ", "qwertyuiop[")
            } else {
                currentPage++
                binding.progg.show()
                mainViewModel.retriveMainVideos(page = 0, pageLimit = 3, ishome = "1")
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
            pauseAllVideos()
//            binding.vidRec.adapter = null
        }

        override fun onStop() {
            super.onStop()
            pauseAllVideos()
//            binding.vidRec.adapter = null


        }

        override fun pauseAllVideos() {
            for (i in 0 until binding.vidRec.childCount) {
                val viewHolder =
                    binding.vidRec.findViewHolderForAdapterPosition(i) as? VideoFeedAdapter.VideoViewHolder
                viewHolder?.player?.pause()
            }
        }

        override fun playVideoAtPosition(position: Int) {
            val viewHolder =
                binding.vidRec.findViewHolderForAdapterPosition(position) as? VideoFeedAdapter.VideoViewHolder
            viewHolder?.player?.play()
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

                // Set rounded corner drawable as background
                botBinding.root.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.buttom_sheet_back)

                // address viewBinding to the bottomSheet dialog
                dialog.setContentView(botBinding.root)

                dialog.show()


            }

        }
    }