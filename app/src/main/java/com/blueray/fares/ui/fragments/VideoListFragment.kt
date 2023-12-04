


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

    private lateinit var binding : FragmentVideoListBinding
    private lateinit var videoAdapter :VideoItemAdapter
    var newArrVideoModel = ArrayList<NewAppendItItems>()
    private lateinit var navController: NavController
    var data : Int? = null


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



        mainViewModel.retriveUserVideos(HelperUtils.getUid(requireContext()),"1","14")
        getMainVidos()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)
//        binding.progressBar.show()

//        mainViewModel.retriveUserVideos(HelperUtils.getUid(requireContext()))
//        mainViewModel.retriveUserVideos("65","1","9")

//        getMainVidos()

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                // Custom action to be performed when back is pressed
//                performCustomBackAction()
//            }
//        })



//
//        // Initial layout as Grid
//        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
//        binding.videosRv.adapter = videoAdapter

    }



    private fun performCustomBackAction() {
binding.videosRv.adapter = null


    }
    fun getMainVidos() {
        mainViewModel.getUserVideos().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    val data = result.data.datass
                    if (data.isNullOrEmpty()) {
                        // Handle empty data scenario
                    } else {
                        val safeData = data.mapNotNull { item ->
                            val adaptiveFile = item.vimeo_detials?.files?.firstOrNull {
                                it.rendition == "adaptive" || it.rendition == "360"
                            }
                            val vidLink = adaptiveFile?.link ?: item.file
                            Log.d("AdaptiveLink", vidLink)

                            NewAppendItItems(
                                item.title ?: "",
                                item.id.toString(),
                                item.created ?: "",
                                vidLink,
                                item.auther?.uid ?: "",
                                item.auther?.username ?: "",
                                item.vimeo_detials?.duration ?: 0,
                                item.vimeo_detials?.pictures?.base_link ?: ""
                                , status = item.moderation_state.toString()
                            )
                        }

                        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
//                    switchToGridLayout()
                        videoAdapter = VideoItemAdapter(1, safeData, object : VideoClick {
                            override fun OnVideoClic(pos: List<NewAppendItItems>, position: Int) {
//                if (!isLinearLayout) {
//                    switchToLinearLayout(position)
//                }
                                // else, handle the video click in linear layout


                                val intent = Intent(context, VidInnerPlay::class.java).apply {
                                    putExtra(
                                        "dataList",
                                        newArrVideoModel
                                    ) // Assuming YourDataType is Serializable or Parcelable
                                    putExtra("position", position)
                                }


                                startActivity(intent)

                            }
                        }, requireContext())


                        binding.videosRv.adapter = videoAdapter
                        binding.progressBar.hide()

                        // rest of your code to set up RecyclerView adapter
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    // Update UI to show error message
                }

                is NetworkResults.NoInternet -> {
                    // Handle no internet scenario, maybe show a message to the user
                }
            }
        }
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