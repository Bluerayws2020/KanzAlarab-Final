package com.blueray.fares.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blueray.fares.adapters.VideoItemAdapter
import com.blueray.fares.api.VideoClick
import com.blueray.fares.databinding.FragmentMyAccountBinding
import com.blueray.fares.databinding.FragmentVideoListBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.NewAppendItItems
import com.blueray.fares.ui.viewModels.AppViewModel
import java.util.ArrayList


class SavedVideoFragment : Fragment() {

    private var isLoading = false
    private var noMoreData = false

    private lateinit var binding : FragmentVideoListBinding
    private lateinit var videoAdapter :VideoItemAdapter
    var newArrVideoModel = ArrayList<NewAppendItItems>()
    private lateinit var navController: NavController
    var data : Int? = null

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
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)
        binding.progressBar.show()

        mainViewModel.retriveFlagContent("save")
        getMainVidos()
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


    fun getMainVidos() {
        mainViewModel.getFlagContent().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    val data = result.data.datass


                    if (result.data.datass.isNullOrEmpty()) {
                        noMoreData = true
                        binding.progressBar.hide()
                        binding.noData.show()
                        binding.videosRv.hide()
                    } else {
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


                        if (newArrVideoModel.isEmpty()) {
                            newArrVideoModel.addAll(safeData)
                            setupRecyclerView(safeData)
                        } else {
                            val startPosition = newArrVideoModel.size
                            newArrVideoModel.addAll(safeData)
                            videoAdapter.notifyItemRangeInserted(startPosition, safeData.size)
                        }
                    }

                    isLoading = false
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
    fun setupRecyclerView(safeData:List<NewAppendItItems>){

        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
//                    switchToGridLayout()
        videoAdapter = VideoItemAdapter(2, newArrVideoModel, object : VideoClick {
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
                startActivity(intent)
            }
        }, requireContext())

        val startPosition = newArrVideoModel.size


        binding.videosRv.adapter = videoAdapter
        binding.progressBar.hide()

//        addScrollListener()
    }
    private fun addScrollListener() {
        binding.videosRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (noMoreData || isLoading) return

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount <= (lastVisibleItem + 3)) {
                    loadMoreItems()
                }
            }
        })
    }
    private fun loadMoreItems() {
        isLoading = true
        currentPage++
        binding.progressBar.show()
        mainViewModel.retriveUserVideos("1", "3", HelperUtils.getUid(requireContext()), "0", currentPage.toString())
    }




}

