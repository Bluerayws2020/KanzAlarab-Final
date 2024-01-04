package com.blueray.fares.ui.fragments



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.fares.R
import com.blueray.fares.adapters.SearchAdapters
import com.blueray.fares.api.OnProfileClick
import com.blueray.fares.api.OnProfileSearch
import com.blueray.fares.databinding.SearchFragmentsBinding
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.ui.viewModels.AppViewModel


class SearchFragment : Fragment() {
    private lateinit var adapter : SearchAdapters
    private lateinit var binding : SearchFragmentsBinding
    private lateinit var navController: NavController


    private val mainViewModel by viewModels<AppViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentsBinding.inflate(layoutInflater)
//        setUpRecyclerView()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
binding.searchs.show()
        getMainVidos()

        binding.noData.show()

        binding.searchs.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Perform your search operation here
                binding.progressBar.show()
                mainViewModel.retrivesearchTxt(binding.searchs.text.toString())
                true
            } else {
                false
            }
        }



    }
//    private fun setUpRecyclerView() {
//        adapter = SearchAdapters(listOf())
//        val lm  = LinearLayoutManager(requireContext())
//        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = lm
//    }


    fun getMainVidos(){
        mainViewModel.getSerchData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
binding.progressBar.hide()


                    if(result.data.datass.isNullOrEmpty()){
                        binding.noData.show()
                    }else {
                        binding.noData.hide()

                    }
                    adapter = SearchAdapters(result.data.datass ,object : OnProfileSearch {
                        override fun onProfileTargetSearch(pos: Int) {
                            var swipedItem  = result.data.datass[pos]
                            val bundle = Bundle().apply {
//                            if (swipedItem.type == "poet") {



                                putString(
                                    "usernName",
                                    swipedItem.user_name
                                ) // Use your item's unique identifier
                                putString(
                                    "userIdes",
                                    swipedItem.uid
                                ) // Use your item's unique identifier
                                putString(
                                    "userImg",
                                    swipedItem.picture
                                ) // Use your item's unique identifier
                                putString(
                                    "fullname",
                                    swipedItem.profile_data.first_name + swipedItem.profile_data.last_name
                                ) // Use your item's unique identifier

                                putString(
                                    "numOfFollowers",
                                    swipedItem.autherFoloower.numOfFollowers.toString()
                                ) // Use your item's unique identifier

                                putString(
                                    "numOfFollowing",
                                    swipedItem.autherFoloower.numOfFollowing.toString()
                                ) // Use your item's unique identifier


                                putString(
                                    "numOfLikes",
                                    swipedItem.autherFoloower.numOfLikes.toString()
                                ) // Use your item's unique identifier


                                putString(
                                    "target_user_follow_flag",
                                    swipedItem.autherFoloower.numOfLikes.toString()
                                ) // Use your item's unique identifier





                            }

                            navController.navigate(R.id.partitionChannelFragment, bundle)


                        }
                    })
                    val lm  = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = adapter
                    binding.recyclerView.layoutManager = lm

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
    }




