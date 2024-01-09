package com.blueray.fares.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.blueray.fares.R
import com.blueray.fares.adapters.ProfilePagerAdapter
import com.blueray.fares.adapters.VideoAdapter
import com.blueray.fares.adapters.VideoFeedAdapter
import com.blueray.fares.adapters.VideoItemAdapter
import com.blueray.fares.api.OnProfileClick
import com.blueray.fares.api.VideoClick
import com.blueray.fares.databinding.FragmentPartitionChannelBinding
import com.blueray.fares.databinding.FragmentVideoListBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.NewAppendItItems
import com.blueray.fares.ui.viewModels.AppViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList


class VideoListFragment : Fragment() {

    private var isLoading = false
    private var isLastPage = false

    private lateinit var binding: FragmentVideoListBinding
    private lateinit var videoAdapter: VideoItemAdapter
    var newArrVideoModel = ArrayList<NewAppendItItems>()
    private lateinit var navController: NavController
    var data: Int? = null

    private var currentPage = 0
    private val pageSize = 3 // Set this based on your API's page size

    private var isLinearLayout = false
    private var lastClickedPosition = 0
    var userIdes = ""
    private val mainViewModel by viewModels<AppViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVideoListBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)



        binding.shimmerView.startShimmer()
        mainViewModel.retriveUserVideos("1", "9", userIdes, "0", currentPage.toString())

        binding.progressBar.show()
        setupRecyclerView()
        getMainVidos()
        return binding.root
    }


//    private fun setupRecyclerView() {
//        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
//        videoAdapter = VideoItemAdapter(1, newArrVideoModel, object : VideoClick {
//            override fun OnVideoClic(pos: List<NewAppendItItems>, position: Int) {
////
////                                val intent = Intent(context, VidInnerPlay::class.java)
////                                intent.putExtra(
////                                        "dataList",
////                                        newArrVideoModel
////                                    ) // Assuming YourDataType is Serializable or Parcelable
////                                intent.putExtra("position", position)
////
////
////
////                                startActivity(intent)
//
//            }
//
//            override fun OnVideoClic(position: Int) {
//                val intent = Intent(context, VidInnerPlay::class.java)
////                    .apply {
////                    putExtra("dataList", newArrVideoModel) // Assuming YourDataType is Serializable or Parcelable
////                    putExtra("position", position)
////                }
//
//                PartitionChannelFragment.DataHolder.itemsList = newArrVideoModel
//
//                intent.putExtra("isMyProfile","1")
//
//                startActivity(intent)
//            }
//        }, requireContext())
//        binding.videosRv.adapter = videoAdapter
//
//    }


    private fun setupRecyclerView() {
        binding.progressBar.show()

        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
        videoAdapter = VideoItemAdapter(1, newArrVideoModel, object : VideoClick {
            override fun OnVideoClic(pos: List<NewAppendItItems>, position: Int) {
//
//                                val intent = Intent(context, VidInnerPlay::class.java)
//                                intent.putExtra(
//                                        "dataList",
//                                        newArrVideoModel
//                                    ) // Assuming YourDataType is Serializable or Parcelable
//                                intent.putExtra("position", position)
//
//
//
//                                startActivity(intent)

            }

            override fun OnVideoClic(position: Int) {
                val intent = Intent(context, VidInnerPlay::class.java)
//                    .apply {
//                    putExtra("dataList", newArrVideoModel) // Assuming YourDataType is Serializable or Parcelable
//                    putExtra("position", position)
//                }

                PartitionChannelFragment.DataHolder.itemsList = newArrVideoModel

                intent.putExtra("isMyProfile", "1")

                startActivity(intent)
            }


        }, requireContext())
        binding.videosRv.adapter = videoAdapter

        binding.videosRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && !isLastPage && lastVisibleItem + 1 >= totalItemCount) {
                    loadMoreItems()
                }
            }
        })

    }


    private fun loadMoreItems() {
        if (!isLoading && !isLastPage) {
            isLoading = true
            binding.progressBar.show()
            mainViewModel.retriveUserVideos("1", "6", userIdes, "0", currentPage.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(emptyList())
    }


    private fun performCustomBackAction() {
        binding.videosRv.adapter = null


    }

    fun getMainVidos() {
        mainViewModel.getUserVideos().observe(viewLifecycleOwner) { result ->

            binding.shimmerView.stopShimmer()
            binding.shimmerView.hide()

            when (result) {

                is NetworkResults.Success -> {

                    val data = result.data.datass

                    if (result.data.datass.isNullOrEmpty()) {
                        binding.progressBar.hide()
                        binding.noData.show()
                        binding.videosRv.hide()
                        isLastPage = true
                        isLoading = true // Reset loading flag here


                    } else {
                        isLoading = false // Reset loading flag here

                        binding.noData.hide()
                        binding.videosRv.show()
                        binding.progressBar.hide()

                        val safeData = data.mapNotNull { item ->
                            val adaptiveFile = item.vimeo_detials?.files?.firstOrNull {
                                it.rendition == "adaptive" || it.rendition == "360"
                            }
                            val vidLink = adaptiveFile?.link ?: item.file
                            Log.d("AdaptiveLink", vidLink)

                            NewAppendItItems(
                                item.title,
                                item.id.toString(),
                                item.created,
                                vidLink,
                                item.auther.uid,
                                item.auther.username,
                                item.vimeo_detials.duration,
                                item.vimeo_detials.pictures?.base_link.toString(),

                                firstName = item.auther.profile_data.first_name,
                                lastName = item.auther.profile_data.last_name,
                                type = item.auther.type,
                                bandNam = item.auther.profile_data.band_name,
                                userPic = item.auther.profile_data.user_picture,
                                status = item.moderation_state,
                                userFav = item.video_actions_per_user.favorites.toString(),
                                userSave = item.video_actions_per_user.save.toString(),
                                target_user = result.data.target_user,
                                video_counts = item.video_counts,
                                numOfFollowers = item.auther.numOfFollowers,
                                numOfFollowing = item.auther.numOfFollowing,
                                numOfLikes = item.auther.numOfLikes


                            )
                        }
                        // Convert each item to NewAppendItItems
                        // (Your existing logic here)


//                        val startPosition = newArrVideoModel.size
//                        newArrVideoModel.addAll(safeData)
//                        videoAdapter.notifyItemRangeInserted(startPosition, data.size)

                        updateRecyclerView(safeData)
                        currentPage++


                    }

                    binding.progressBar.hide()
                }
                is NetworkResults.Error -> {
                    // Handle error case
                }
                is NetworkResults.NoInternet -> {
                    // Handle no internet case
                }
            }
        }
    }


    fun setupRecyclerView(safeData: List<NewAppendItItems>) {

        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
//                    switchToGridLayout()
        videoAdapter = VideoItemAdapter(1, newArrVideoModel, object : VideoClick {
            override fun OnVideoClic(pos: List<NewAppendItItems>, position: Int) {
//
//                                val intent = Intent(context, VidInnerPlay::class.java)
//                                intent.putExtra(
//                                        "dataList",
//                                        newArrVideoModel
//                                    ) // Assuming YourDataType is Serializable or Parcelable
//                                intent.putExtra("position", position)
//
//
//
//                                startActivity(intent)

            }

            override fun OnVideoClic(position: Int) {
                val intent = Intent(context, VidInnerPlay::class.java)
//                    .apply {
//                    putExtra("dataList", newArrVideoModel) // Assuming YourDataType is Serializable or Parcelable
//                    putExtra("position", position)
//                }

                PartitionChannelFragment.DataHolder.itemsList = newArrVideoModel

                intent.putExtra("isMyProfile", "1")

                startActivity(intent)
            }
        }, requireContext())

        val startPosition = newArrVideoModel.size


        binding.videosRv.adapter = videoAdapter

    }


    private fun updateRecyclerView(newItems: List<NewAppendItItems>) {
        val startPosition = newArrVideoModel.size
        newArrVideoModel.addAll(newItems)
        videoAdapter.notifyItemRangeInserted(startPosition, newItems.size)
        binding.progressBar.hide()
    }


    override fun onResume() {
        super.onResume()
        // Handle back press or similar action
        // For example, listen for a back button press in the toolbar
//        binding.includeTap.menu.setNavigationOnClickListener {
//            if (isLinearLayout) {
//                switchToGridLayout()
//            } else {
//                // Handle regular back action
//                navController.navigateUp()
//            }
//        }

    }


}