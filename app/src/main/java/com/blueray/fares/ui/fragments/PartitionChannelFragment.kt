package com.blueray.fares.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
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
import com.blueray.fares.adapters.FollowersPagerAdapter
import com.blueray.fares.adapters.ProfilePagerAdapter
import com.blueray.fares.adapters.VideoAdapter
import com.blueray.fares.adapters.VideoFeedAdapter
import com.blueray.fares.adapters.VideoItemAdapter
import com.blueray.fares.api.OnProfileClick
import com.blueray.fares.api.VideoClick
import com.blueray.fares.databinding.FragmentPartitionChannelBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.NewAppendItItems
import com.blueray.fares.ui.activities.FollowingAndFollowersActivity
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
    object DataHolder {
        var itemsList: ArrayList<NewAppendItItems>? = null
    }

    private var isUserInteraction = false

    private lateinit var binding: FragmentPartitionChannelBinding
    private lateinit var videoAdapter: VideoItemAdapter
    var newArrVideoModel = ArrayList<NewAppendItItems>()
    private lateinit var navController: NavController
    private var isLoading = false
    private var noMoreData = false

    private var currentPage = 0
    var target_user_follow_flag = ""
    private var isLinearLayout = false
    private var lastClickedPosition = 0
    var userIdes = ""
    var userName  = ""
    private val mainViewModel by viewModels<AppViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPartitionChannelBinding.inflate(layoutInflater)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        // Retrieve the passed data
        userName = arguments?.getString("usernName").toString()
        userIdes = arguments?.getString("userIdes").toString()
        val userImg = arguments?.getString("userImg").toString()
        val fullname = arguments?.getString("fullname")

        val numOfLikes = arguments?.getString("numOfLikes")
        val numOfFollowing = arguments?.getString("numOfFollowing")
        val numOfFollowers = arguments?.getString("numOfFollowers")
        target_user_follow_flag = arguments?.getString("isUserFollower").toString()

        binding.numFolloweing.text = numOfFollowing ?: "0"

        binding.numFollowers.text = numOfFollowers ?: "0"
        binding.numOfLike.text = numOfLikes ?: "0"
binding.includeTap.title.text  = fullname
        getUserAction()


/// inistial State

        Log.d("TEEEEES", userIdes)

        binding.followCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Checkbox is checked
                mainViewModel.retriveSetAction(userIdes, "user", "following")
                binding.followCheckbox.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                binding.followCheckbox.text = "الغاء المتابعة"
            } else {
                // Checkbox is not checked
                mainViewModel.retriveSetAction(userIdes, "user", "following") // Assuming you have an unfollowing action
                binding.followCheckbox.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                binding.followCheckbox.text = "متابعة"
            }
        }

        // Revert changes for "Follow" state








//            if (HelperUtils.getUid(requireContext()) == userIdes){
//                binding.followBtn.text =  "الغاء المتابعة"
//                binding.followBtn.setBackgroundResource(R.drawable.un_follow)
//                mainViewModel.retriveSetAction("1", "user", "following")
//
//
//            }else {
//                binding.followBtn.text = "متابعة"
//                binding.followBtn.setBackgroundResource(R.drawable.un_follow)
//                mainViewModel.retriveSetAction("1", "user", "following")
//
//
//            }

            Glide.with(requireContext()).load(userImg).placeholder(R.drawable.logo).into(binding.profileImage)


            if (userName != null) {
                // Use the retrieved itemId
                // For example, load data based on this itemId or initialize views
                binding.userName.text = "@$userName"
//                binding.name.text = fullname

            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.progressBar.show()

        mainViewModel.retriveUserVideos("0","6",userIdes,"1",currentPage.toString())
        getMainVidos()
binding.followCheckbox.hide()

        binding.followersLayout.setOnClickListener {
            val intent  = Intent(requireContext(), FollowingAndFollowersActivity::class.java)
            intent.putExtra("user_id", HelperUtils.getUid(requireContext())) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("userName",userName ) // Replace 'yourUserId' with the actual user ID

            startActivity(intent)


        }

        binding.followingLayout.setOnClickListener {
            val intent  = Intent(requireContext(), FollowingAndFollowersActivity::class.java)
            intent.putExtra("user_id", HelperUtils.getUid(requireContext())) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("userName",userName ) // Replace 'yourUserId' with the actual user ID

            startActivity(intent)
        }
//
//        // Initial layout as Grid
//        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
//        binding.videosRv.adapter = videoAdapter

    }


    private fun getUserAction() {

        mainViewModel.getSetAction().observe(viewLifecycleOwner) { result ->

            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
//                        Toast.makeText(
//                            requireContext(),
//                            result.data.msg.toString(),
//                            Toast.LENGTH_LONG
//                        ).show()

//                        if(result.data.msg == "unfollowing success"){
//                            binding.followCheckbox.isChecked =  true
//                            binding.followCheckbox.text = "متابعة"
//
//                        //
//                        }else {
//                            binding.followCheckbox.isChecked =  false
//                            binding.followCheckbox.text =  "الغاء المتابعة"
//
//
//
//
//
//                        }


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


        fun getMainVidos(){
            mainViewModel.getUserVideos().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is NetworkResults.Success -> {
if (result.data.datass.isNullOrEmpty()){
    binding.noData.show()
    binding.videosRv.hide()

}else {
    binding.noData.hide()
binding.videosRv.show()


}
                        binding.followCheckbox.show()


//                        target_user_follow_flag = result.data.target_user?.target_user_follow_flag.toString()
                        if (result.data.target_user?.target_user_follow_flag != 1){
                            binding.followCheckbox.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                            binding.followCheckbox.text = "متابعة"

                            binding.followCheckbox.isChecked = true
                        }else{
                            binding.followCheckbox.isChecked = false
                            binding.followCheckbox.text =  "الغاء المتابعة"

                            binding.followCheckbox.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

                        }

                        Log.d("ertyui",target_user_follow_flag)

                        result.data.datass.forEach { item ->
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
                                    item.vimeo_detials.pictures?.base_link.toString(),
                                    firstName = item.auther.profile_data.first_name,
                                    lastName = item.auther.profile_data.last_name,
                                    type = item.auther.type,
                                    bandNam = item.auther.profile_data.band_name,
                                    userPic = item.auther.profile_data.user_picture,
                                    userFav = item.video_actions_per_user.favorites.toString(),
                                    userSave = item.video_actions_per_user.save.toString(),
                                    target_user = result.data.target_user,
                                    video_counts =  item.video_counts,
                                    nodeId = item.id
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




                            }

                            override fun OnVideoClic(position: Int) {
                                val intent = Intent(context, VidInnerPlay::class.java)
//                                    .apply {
//                                    putExtra("dataList", newArrVideoModel) // Assuming YourDataType is Serializable or Parcelable
//                                    putExtra("position", position)
//                                }

DataHolder.itemsList = newArrVideoModel
                                startActivity(intent)
                            }
                        }, requireContext())
                        binding.videosRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    private fun loadMoreItems() {
        if (noMoreData) {
                Log.d("No MOREEE DATA ", "qwertyuiop[")
        } else {
            currentPage++
            binding.progressBar.show()
            mainViewModel.retriveUserVideos("0","3",userIdes,"1",currentPage.toString())
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