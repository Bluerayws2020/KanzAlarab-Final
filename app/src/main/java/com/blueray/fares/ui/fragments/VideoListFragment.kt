package com.blueray.fares.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.fares.R
import com.blueray.fares.adapters.VideoItemAdapter
import com.blueray.fares.databinding.FragmentPartitionChannelBinding
import com.blueray.fares.databinding.FragmentVideoListBinding
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.NewAppendItItems
import com.blueray.fares.ui.viewModels.AppViewModel
import com.google.android.material.progressindicator.BaseProgressIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList

class VideoListFragment : Fragment() {

    private lateinit var binding : FragmentVideoListBinding
    private lateinit var videoAdapter :VideoItemAdapter
    var newArrVideoModel = ArrayList<NewAppendItItems>()

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
        getVideosView()
        mainViewModel.retriveMainVideos()
        getNewItems()

    }

    fun getVideosView(){
        mainViewModel.getMainVideos().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    GlobalScope.launch(Dispatchers.IO) {
                        val deferredResults = result.data.map { item ->
                            async {

                                val lastItemNumber = item.file.substringAfterLast("/")



                                mainViewModel.retrieveVideoOption("https://api.vimeo.com/videos/$lastItemNumber",item.token) // Replace with your actual API call


                            }
                        }
                    }

                }
                is NetworkResults.Error -> {
//                    binding.swipeToRefresh.isRefreshing = true

                    result.exception.printStackTrace()
                    Log.d("=lol",result.exception.toString())
                }

                is NetworkResults.NoInternet -> TODO()
            }
        }

    }


    fun getNewItems(){
        mainViewModel.getVideoOption().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.files.isNullOrEmpty()){

                    }else {
                        val videoOptionList = result.data.files
                        newArrVideoModel.add(
                            NewAppendItItems(
                                "134",
                                "312", videoOptionList.last().link.toString()
                            )
                        )
//                    videoAdapter = VideoAdapter(newArrVideoModel)
                        binding.videosRv.layoutManager = GridLayoutManager(requireContext(),3)

                        videoAdapter = VideoItemAdapter(newArrVideoModel,{
//
                        },requireContext())

                        binding.videosRv.adapter = videoAdapter}

//                            ,object : OnProfileClick {
//                            override fun onProfileClikc(pos: Int) {
//
//                                Log.d("TEEEESSSSTTT11",newArrVideoModel[pos].videoUrl.toString())
//                            }
//
//                            override fun onProfileShare(pos: Int) {
//                                TODO("Not yet implemented")
//                            }
//                        })
//
//                        binding.vidRec.adapter = videoAdapter
//
//
//                        // pager snap helper is a class that helps to move the recycler one item at at a time
////
//
//                        binding.vidRec.layoutManager = LinearLayoutManager(
//                            context,
//                            LinearLayoutManager.VERTICAL, true
//                        )// trying reversed layout
//
//                    }

//                for (i in videoOptionList){
//                }

//                if (isAdded)

//                    showDialog(videoOptionList)
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }

                is NetworkResults.NoInternet -> TODO()
            }
        }
    }
}