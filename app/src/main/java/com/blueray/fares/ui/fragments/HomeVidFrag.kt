package com.blueray.fares.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.blueray.fares.api.OnProfileClick
import com.blueray.fares.databinding.OnevidfragBinding
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.NewAppendItItems
import com.blueray.fares.ui.viewModels.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class HomeVidFrag : Fragment() {
    private lateinit var navController: NavController

    private lateinit var binding : OnevidfragBinding
    var arrVideoModel = ArrayList<NewAppendItItems>()
    var newArrVideoModel = ArrayList<NewAppendItItems>()

    var videoAdapter: VideoAdapter? = null
    private val mainViewModel by viewModels<AppViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

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
binding.includeTap.profile.setOnClickListener {
    navController.navigate(R.id.yourChannelFragment)


//
}


        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false // We are not interested in move events for this example.
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Handle the swipe event, navigate to new fragment
                val position = viewHolder.adapterPosition // Get swiped item position
                navController.navigate(R.id.partitionChannelFragment)
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



        getVideosView()
        mainViewModel.retriveMainVideos()
        getNewItems()



        return binding.root


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
                    videoAdapter = VideoAdapter(newArrVideoModel,object :OnProfileClick{
                        override fun onProfileClikc(pos: Int) {

Log.d("TEEEESSSSTTT11",newArrVideoModel[pos].videoUrl.toString())
                        }

                        override fun onProfileShare(pos: Int) {
                            TODO("Not yet implemented")
                        }
                    })

                    binding.vidRec.adapter = videoAdapter


                    // pager snap helper is a class that helps to move the recycler one item at at a time
//

                    binding.vidRec.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL, true
                    )// trying reversed layout

                }

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