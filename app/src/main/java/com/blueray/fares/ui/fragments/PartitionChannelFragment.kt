package com.blueray.fares.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.NewAppendItItems
import com.blueray.fares.ui.viewModels.AppViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList


class PartitionChannelFragment : Fragment() {

    private lateinit var binding : FragmentPartitionChannelBinding
    private lateinit var videoAdapter :VideoItemAdapter
    var newArrVideoModel = ArrayList<NewAppendItItems>()
    private lateinit var navController: NavController


    private var isLinearLayout = false
    private var lastClickedPosition = 0
    var userIdes = ""
    private val mainViewModel by viewModels<AppViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPartitionChannelBinding.inflate(layoutInflater)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val bundle = Bundle().apply {







            // Retrieve the passed data
            val usernName = arguments?.getString("usernName")
             userIdes = arguments?.getString("userIdes").toString()
            val userImg = arguments?.getString("userImg").toString()
            val fullname = arguments?.getString("fullname")





            Glide.with(requireContext()).load(userImg).placeholder(R.drawable.logo).into(binding.profileImage)


            if (usernName != null) {
                // Use the retrieved itemId
                // For example, load data based on this itemId or initialize views
                binding.userName.text = "@$usernName"
//                binding.name.text = fullname

            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.progressBar.show()

        mainViewModel.retriveUserVideos(userIdes,"0","9")
        getMainVidos()

//
//        // Initial layout as Grid
//        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
//        binding.videosRv.adapter = videoAdapter

    }




        fun getMainVidos(){
            mainViewModel.getUserVideos().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is NetworkResults.Success -> {

                        result.data.forEach { item ->
                            var vidLink = ""
                            val adaptiveFile = item.vimeo_detials.files.firstOrNull { it.rendition == "adaptive" || it.rendition == "360" }
                            vidLink = adaptiveFile?.link ?: item.file


                            Log.d("AdaptiveLink",vidLink)

                            newArrVideoModel.add(
                                NewAppendItItems(
                                    item.title,
                                    item.id.toString(),
                                    item.created,
                                    vidLink,
                                    item.auther.uid,
                                    item.auther.username,
                                    item.vimeo_detials.duration,
                                    item.vimeo_detials.pictures.base_link
                                )
                            )

                            }
                            binding.videosRv.layoutManager = GridLayoutManager(requireContext(),3)
    //                    switchToGridLayout()
                        videoAdapter = VideoItemAdapter(0,newArrVideoModel, object : VideoClick {
                            override fun OnVideoClic(pos: List<NewAppendItItems>, position: Int) {
    //                if (!isLinearLayout) {
    //                    switchToLinearLayout(position)
    //                }
                                // else, handle the video click in linear layout


                                val intent = Intent(context, VidInnerPlay::class.java).apply {
                                    putExtra("dataList", newArrVideoModel) // Assuming YourDataType is Serializable or Parcelable
                                    putExtra("position", position)
                                }


                                startActivity(intent)

                            }
                        }, requireContext())


                        binding.videosRv.adapter = videoAdapter
                        binding.progressBar.hide()

                    }


                    is NetworkResults.Error -> {
                        result.exception.printStackTrace()
                        binding.progressBar.hide()

                    }

                    is NetworkResults.NoInternet -> TODO()
                }
            }
        }
    private fun switchToLinearLayout(position: Int) {
        isLinearLayout = true
        videoAdapter.setLinearLayoutMode(true)
        binding.videosRv.layoutManager = LinearLayoutManager(requireContext())
        binding.videosRv.adapter = videoAdapter
        binding.videosRv.scrollToPosition(position)
        binding.constraintLayoutHeader.hide()
        videoAdapter.notifyDataSetChanged()



    }

    private fun switchToGridLayout() {
        isLinearLayout = false
        videoAdapter.setLinearLayoutMode(false)
        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.videosRv.adapter = videoAdapter
        binding.constraintLayoutHeader.show()
        binding.videosRv.scrollToPosition(lastClickedPosition)
        videoAdapter.notifyDataSetChanged()

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